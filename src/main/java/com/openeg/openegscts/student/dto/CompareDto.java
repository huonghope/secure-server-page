package com.openeg.openegscts.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareDto {

    private String scodeId;
    private long secId;
    private String scodeLineNum;
    private String scodeSecFile;
    private String compileCode;
    private String scodeSecCode;
    private String scodeKeyword;
    private long solvedId;
    private String userId;
    private String languageType;

}
