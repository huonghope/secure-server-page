package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.student.dto.SolvedCodeDto;
import com.openeg.openegscts.student.entity.*;
import com.openeg.openegscts.student.repository.ICodeInfoMapper;
import com.openeg.openegscts.file.FileGet;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CodeInfoServiceImpl implements ICodeInfoService {

    ICodeInfoMapper iCodeInfoMapper;
    ModelMapper modelMapper = new ModelMapper();
    FileGet fileGet;

    @Autowired
    public CodeInfoServiceImpl(ICodeInfoMapper iCodeInfoMapper, FileGet fileGet) {
        this.iCodeInfoMapper = iCodeInfoMapper;
        this.fileGet = fileGet;
    }

    @Override
    public SecurityCodeDto getSecurityInfo(long secId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SecurityCode securityInfo = iCodeInfoMapper.getSecurityInfo(secId);

        if (securityInfo == null) {
            log.info("not exists security vulnerability information");
            return null;
        }

        SecurityCodeDto returnValue = modelMapper.map(securityInfo, SecurityCodeDto.class);
        return returnValue;
    }

    @Override
    public SolvedCodeDto getStepInfo(String scodeId, String userId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SolvedCode stepInfo = iCodeInfoMapper.getStepInfo(scodeId, userId);

        if (stepInfo == null) {
            return null;
        }

        SolvedCodeDto returnValue = modelMapper.map(stepInfo, SolvedCodeDto.class);
        return returnValue;
    }

    @Override
    public List<CodeList> getCodeListBySecId(Compare compare) {

        List<CodeList> clEntity = iCodeInfoMapper.getCodeListBySecId(compare);

        if(clEntity == null) {
            log.info(String.format("not exists code list by secId"));
            return new ArrayList<>();
        }

        return clEntity;
    }
}
