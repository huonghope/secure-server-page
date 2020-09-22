package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.*;
import com.openeg.openegscts.student.entity.Compare;
import com.openeg.openegscts.student.entity.InsertSolvedCode;
import com.openeg.openegscts.student.entity.SubmitResult;
import com.openeg.openegscts.student.repository.ICompareMapper;
import com.openeg.openegscts.student.repository.IUserMapper;
import com.openeg.openegscts.solution.CompareCode;
import com.openeg.openegscts.solution.CompareLineNumber;
import com.openeg.openegscts.solution.CompileThread;
import com.openeg.openegscts.file.CompileSave;
import com.openeg.openegscts.file.FileGet;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CompareServiceImpl implements ICompareService {

    Environment env;
    ICompareMapper iCompareMapper;
    IUserMapper iUserMapper;
    FileGet fileGet;
    CompareLineNumber compareLineNumber;
    CompareCode compareCode;
    CompileSave compileSave;
    ModelMapper modelMapper = new ModelMapper();
    int compileSaveResult = 0;
    int compileResult = 0;
    boolean compareResult = false;

    @Autowired
    public CompareServiceImpl(ICompareMapper iCompareMapper, IUserMapper iUserMapper, FileGet fileGet, CompareLineNumber compareLineNumber, CompareCode compareCode, CompileSave compileSave, Environment env) {
        this.iCompareMapper = iCompareMapper;
        this.iUserMapper = iUserMapper;
        this.fileGet = fileGet;
        this.compareLineNumber = compareLineNumber;
        this.compareCode = compareCode;
        this.compileSave = compileSave;
        this.env = env;
    }

    @Override
    public CompareDto getLineNum(String scodeId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Compare getLineNum = iCompareMapper.getLineNum(scodeId);

        if (getLineNum == null) {
            log.info("not exists line number");
            return null;
        }

        CompareDto returnValue = modelMapper.map(getLineNum, CompareDto.class);
        return returnValue;
    }

    @Override
    public CompareDto getKeyword(String scodeId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Compare getKeyword= iCompareMapper.getKeyword(scodeId);

        if (getKeyword == null) {
            log.info("not exists security code keyword");
            return null;
        }

        CompareDto returnValue = modelMapper.map(getKeyword, CompareDto.class);
        return returnValue;
    }


    @Override
    public CorrectResultDto compareLineNum(CompareDto compareDto) {

        boolean compareResult;
        long getSolvedId = insertSolvedCode(compareDto);

        iCompareMapper.updateLastTryDate(getSolvedId);
        iCompareMapper.updateFirstTryNum(getSolvedId);
        iCompareMapper.updateTotalTryNum(compareDto.getScodeId());
        CompareDto getLineNumData = getLineNum(compareDto.getScodeId());
        compareResult = compareLineNumber.compareLimeNumber(compareDto, getLineNumData);

        if(compareResult == true) {
            log.info("correct");
            CorrectResultDto correctResultDto = getCorrectResult(compareDto.getScodeId(), compareDto.getUserId());
            iCompareMapper.updateFirstSolvedDate(getSolvedId);
            iCompareMapper.updateTotalSuccNum(compareDto.getScodeId());
            return correctResultDto;
        } else {
            log.info("incorrect");
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            IncorrectResultDto incorrectResultDto = getIncorrectResult(compareDto.getScodeId(), compareDto.getUserId());
            CorrectResultDto returnValue = modelMapper.map(incorrectResultDto, CorrectResultDto.class);
            return returnValue;
        }

    }

    @Override
    public CorrectResultDto compareSolution(CompareDto compareDto) throws InterruptedException {

        String getScodeId = compareDto.getScodeId();
        String getUserId = compareDto.getUserId();
        CompareDto getKeyword = getKeyword(getScodeId);
        String getFileName = getKeyword.getScodeSecFile();
        String getScodeSecFile = getKeyword.getScodeSecFile();
        String getPath = env.getProperty("compile.path");

        iCompareMapper.updateLastTryDate2(getScodeId, getUserId);
        iCompareMapper.updateSecondTryNum(getScodeId, getUserId);
        iCompareMapper.updateTotalTryNum(getScodeId);
        compileSaveResult = compileSave.compileSave(compareDto, getScodeSecFile);

        if(compileSaveResult == 1) {
            CompileThread compileThread = new CompileThread(getPath, getFileName, getUserId);
            Thread thread = new Thread(compileThread, "CompileThread");
            thread.start();
            thread.join();
            compileResult = compileThread.getReturnValue(); // 1 or 2
        } else if (compileSaveResult == 0) {
            log.info("Failed file save");
        }

        if (compileResult == 1) {
            log.info("Success compile");
            compareResult = compareCode.compareCode(compareDto, getKeyword);
        } else if (compileResult == 2) {
            log.info("Failed compile result");
        }

        if(compareResult == true) {
            log.info("correct");
            CorrectResultDto correctResultDto = getCorrectResult(getScodeId, getUserId);
            iCompareMapper.updateSecondSolvedDate(getScodeId, getUserId);
            iCompareMapper.updateTotalSuccNum(getScodeId);
            compareResult = false;
            return correctResultDto;
        } else {
            log.info("incorrect");
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            IncorrectResultDto incorrectResultDto = getIncorrectResult(getScodeId, getUserId);
            CorrectResultDto returnValue = modelMapper.map(incorrectResultDto, CorrectResultDto.class);

            if(compileResult == 2) {
                returnValue.setIsError(1); // 컴파일 에러
            } else {
                returnValue.setIsError(0); // 패턴 에러
            }
            return returnValue;

        }

    }

    @Override
    public CorrectResultDto getCorrectResult(String scodeId, String userId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SubmitResult submitResult = iCompareMapper.getSubmitResult(scodeId, userId);

        if (submitResult == null) {
            log.info("not exists correct submit result");
            return null;
        }

        CorrectResultDto returnValue = modelMapper.map(submitResult, CorrectResultDto.class);
        return returnValue;
    }

    @Override
    public IncorrectResultDto getIncorrectResult(String scodeId, String userId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SubmitResult submitResult = iCompareMapper.getSubmitResult(scodeId, userId);

        if (submitResult == null) {
            log.info("not exists incorrect submit result");
            return null;
        }

        IncorrectResultDto returnValue = modelMapper.map(submitResult, IncorrectResultDto.class);
        return returnValue;
    }

    @Override
    public long insertSolvedCode(CompareDto compareDto) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Compare cpEntity = modelMapper.map(compareDto, Compare.class);
        iCompareMapper.insertSolvedCode(cpEntity);
        InsertSolvedCode getSolvedId = iCompareMapper.getInsertSolvedCode(cpEntity.getScodeId(), cpEntity.getUserId());

        return getSolvedId.getSolvedId();
    }
}
