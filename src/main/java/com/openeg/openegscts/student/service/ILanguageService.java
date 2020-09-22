package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.LanguageDto;
import com.openeg.openegscts.student.entity.Language;
import com.openeg.openegscts.student.entity.SecurityVulName;

import java.util.List;

public interface ILanguageService {

    LanguageDto insertLanguage(LanguageDto languageDto);
    List<Language> getLanList();
    List<SecurityVulName> getSecNameList(String languageId);
    int deleteLanguage(String languageId);
}
