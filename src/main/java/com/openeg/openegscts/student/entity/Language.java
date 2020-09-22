package com.openeg.openegscts.student.entity;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    private String languageId;
    private String languageType;
    private Date languageRegDate;

    public Language(String languageType) {
        this.languageType = languageType;
    }
}
