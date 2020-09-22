package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.LanguageDto;
import com.openeg.openegscts.student.entity.Language;
import com.openeg.openegscts.student.entity.SecurityVulName;
import com.openeg.openegscts.student.repository.ILanguageMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LanguageServiceImpl implements ILanguageService {

    Environment env;
    ILanguageMapper languageMapper;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public LanguageServiceImpl(ILanguageMapper languageMapper, Environment env) {
        this.languageMapper = languageMapper;
        this.env = env;
    }

    @Override
    public LanguageDto insertLanguage(LanguageDto languageDto) {

        String languageName = languageDto.getLanguageType().replaceAll("\\.", "");
        String COMM_FILE_PATH = env.getProperty("save.code.path") + languageName;
        File folder = new File(COMM_FILE_PATH);

        languageDto = LanguageDto.builder()
                    .languageId(UUID.randomUUID().toString())
                    .languageType(languageDto.getLanguageType())
                    .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Language languageEntity = modelMapper.map(languageDto, Language.class);
        languageMapper.insertLanguage(languageEntity);

        if(!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception e) {
            }
        }

        LanguageDto returnValue = modelMapper.map(languageEntity, LanguageDto.class);
        return returnValue;
    }

    @Override
    public int deleteLanguage(String languageId) {
        int deleteResult = languageMapper.deleteLanguage(languageId);
        return deleteResult;
    }

    @Override
    public List<Language> getLanList() {

        List<Language> lanEntity = languageMapper.getLanList();

        if(lanEntity == null) {
            log.info(String.format("not exists language list"));
            return new ArrayList<>();
        }

        return lanEntity;
    }

    @Override
    public List<SecurityVulName> getSecNameList(String languageId) {
        List<SecurityVulName> securityVulNames = languageMapper.getSecNameList(languageId);

        if(securityVulNames == null) {
            log.info(String.format("not exists Security Vulnerability Name list"));
            return new ArrayList<>();
        }

        return securityVulNames;
    }
}
