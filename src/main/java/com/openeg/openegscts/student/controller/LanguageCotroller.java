package com.openeg.openegscts.student.controller;

import com.openeg.openegscts.student.dto.LanguageDto;
import com.openeg.openegscts.student.entity.Language;
import com.openeg.openegscts.student.entity.SecurityVulName;
import com.openeg.openegscts.student.service.ILanguageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class LanguageCotroller {

    ILanguageService languageService;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public LanguageCotroller(ILanguageService languageService) {
        this.languageService = languageService;
    }

    @PostMapping("/admin/lan")
    public ResponseEntity<LanguageDto> createLanguage(@ModelAttribute Language language) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        LanguageDto languageDto = modelMapper.map(language, LanguageDto.class);
        LanguageDto returnValue = languageService.insertLanguage(languageDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @DeleteMapping("/admin/lan/{languageId}")
    public int deleteLanguage(@PathVariable String languageId) {
        int deleteResult = languageService.deleteLanguage(languageId);
        return deleteResult;
    }

    @GetMapping("/lan")
    public @ResponseBody
    List<Language> getLanList() {

        List<Language> returnValue = new ArrayList<>();
        List<Language> languageList = languageService.getLanList();

        if(languageList == null || languageList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<Language>>(){}.getType();

        returnValue = new ModelMapper().map(languageList, listType);
        log.info("Returning " + returnValue.size() + " language type list");

        return returnValue;
    }

    @GetMapping("/lan/{languageId}")
    public @ResponseBody
    List<SecurityVulName> getSVList(@PathVariable String languageId) {
        List<SecurityVulName> returnValue = new ArrayList<>();
        List<SecurityVulName> secNameList = languageService.getSecNameList(languageId);

        if(secNameList == null || secNameList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<SecurityVulName>>(){}.getType();

        returnValue = new ModelMapper().map(secNameList, listType);
        log.info("Returning " + returnValue.size() + " Security Vulnerability Name list");

        return returnValue;
    }
}
