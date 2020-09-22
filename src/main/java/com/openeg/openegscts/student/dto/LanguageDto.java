package com.openeg.openegscts.student.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LanguageDto {

    private String languageId;
    private String languageType;

    @Builder
    public LanguageDto(String languageId, String languageType) {
        this.languageId = languageId;
        this.languageType = languageType;
    }
}
