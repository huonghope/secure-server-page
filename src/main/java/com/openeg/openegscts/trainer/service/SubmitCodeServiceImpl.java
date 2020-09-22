package com.openeg.openegscts.trainer.service;

import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.trainer.dto.SubmitCodeDto;
import com.openeg.openegscts.trainer.entity.SubmitCode;
import com.openeg.openegscts.file.FileGet;
import com.openeg.openegscts.trainer.repository.ISubmitCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SubmitCodeServiceImpl implements ISubmitCodeService {

    Environment env;
    ISubmitCodeMapper iSubmitCodeMapper;
    ModelMapper modelMapper = new ModelMapper();
    FileGet fileGet;

    @Autowired
    public SubmitCodeServiceImpl(ISubmitCodeMapper iSubmitCodeMapper, FileGet fileGet, Environment env) {
        this.iSubmitCodeMapper = iSubmitCodeMapper;
        this.fileGet = fileGet;
        this.env = env;
    }

    @Override
    public SubmitCodeDto insertSubmitCode(SubmitCodeDto submitCodeDto) {

        submitCodeDto.setScodeId(UUID.randomUUID().toString());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        SubmitCode sbEntity = modelMapper.map(submitCodeDto, SubmitCode.class);
        iSubmitCodeMapper.insertSubmitCode(sbEntity);

        SubmitCodeDto returnValue = modelMapper.map(sbEntity, SubmitCodeDto.class);
        return returnValue;
    }

    @Override
    public List<SubmitCode> getSVbyCategory(long scategoryId) {
        List<SubmitCode> getSV = iSubmitCodeMapper.getSVbyCategory(scategoryId);
        return getSV;
    }

    @Override
    public SubmitCode getLastData() {
        SubmitCode result = iSubmitCodeMapper.getLastData();

        if(result == null) {
            SubmitCode submitCode = new SubmitCode(0);
            return submitCode;
        }

        return result;
    }

    @Override
    public List<SubmitCode> getSubmitCodeList(String userId) {
        List<SubmitCode> allSubmitCodeList  = iSubmitCodeMapper.getSubmitCodeList(userId);

        if(allSubmitCodeList == null) {
            log.info(String.format("not exists submit code list"));
            return new ArrayList<>();
        }

        return allSubmitCodeList;
    }

    @Override
    public SubmitCodeDto getSubmitCode(String scodeId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SubmitCode getSbEntity = iSubmitCodeMapper.getSubmitCode(scodeId);

        if(getSbEntity == null) {
            log.info(String.format("not exists submit code"));
            return null;
        }

        SubmitCodeDto returnValue = modelMapper.map(getSbEntity, SubmitCodeDto.class);

        int idx1 = getSbEntity.getScodeVulFile().lastIndexOf("-");
        String vulFileName = returnValue.getScodeVulFile().substring(idx1 + 1);

        int idx2 = getSbEntity.getScodeSecFile().lastIndexOf("-");
        String secFileName = returnValue.getScodeSecFile().substring(idx2 + 1);

        String getVulFile = fileGet.getFile(env.getProperty("save.code.path") + returnValue.getLanguageType() + "/" + returnValue.getSecId() + "/" + returnValue.getScodeVulFile(), getSbEntity.getLanguageType());
        returnValue.setScodeVulCode(null);
        returnValue.setScodeVulCode(getVulFile);
        returnValue.setScodeVulFile(vulFileName);

        String getSecFile = fileGet.getFile(env.getProperty("save.code.path") + returnValue.getLanguageType() + "/" + returnValue.getSecId() + "/" + returnValue.getScodeSecFile(), getSbEntity.getLanguageType());
        returnValue.setScodeSecCode(null);
        returnValue.setScodeSecCode(getSecFile);
        returnValue.setScodeSecFile(secFileName);

        return returnValue;

    }

    @Override
    public SubmitCodeDto updateSubmitCode(SubmitCodeDto submitCodeDto) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        iSubmitCodeMapper.updateSubmitCode(submitCodeDto);

        SubmitCode sbEntity = iSubmitCodeMapper.getSubmitCode(submitCodeDto.getScodeId());
        return new ModelMapper().map(sbEntity, SubmitCodeDto.class);
    }

    @Override
    public @ResponseBody String deleteSubmitCode(String scodeId) {

        String successMessage = "Delete Success";
        String failMessage = "Delete Fail";
        SubmitCode getSubmitCode = iSubmitCodeMapper.getSubmitCode(scodeId);
        String vulFilePath = env.getProperty("save.code.path") + getSubmitCode.getLanguageType() + "/" + getSubmitCode.getSecId() + "/" + getSubmitCode.getScodeVulFile();
        String secFilePath = env.getProperty("save.code.path") + getSubmitCode.getLanguageType() + "/" + getSubmitCode.getSecId() + "/" + getSubmitCode.getScodeSecFile();
        File vulFile = new File(vulFilePath);
        File secFile = new File(secFilePath);

        try {
            if(vulFile.exists() && secFile.exists()) {
                if(vulFile.delete() && secFile.delete()) {
                    iSubmitCodeMapper.deleteSubmitCode(scodeId);
                    return successMessage;
                } else {
                    return failMessage;
                }
            }
        } catch(Exception e) {
            e.getStackTrace();
        }

        return "";
    }

    @Override
    public Users getUserEmail(String userId) {
        Users getEmail = iSubmitCodeMapper.getUserEmail(userId);
        return getEmail;
    }
}