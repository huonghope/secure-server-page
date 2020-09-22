package com.openeg.openegscts.student.repository;

import com.openeg.openegscts.student.entity.Language;
import com.openeg.openegscts.student.entity.SecurityVulName;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ILanguageMapper {

    int insertLanguage(Language language);
    int deleteLanguage(String languageId);
    List<Language> getLanList();
    List<SecurityVulName> getSecNameList(String languageId);

}