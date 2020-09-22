package com.openeg.openegscts.student.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compare {

    private String scodeId;
    private long secId;
    private String scodeLineNum;
    private String scodeSecFile;
    private String scodeKeyword;
    private String languageId;
    private long solvedId;
    private String userId;
    private String languageType;

    public Compare(String scodeLineNum) {
        this.scodeLineNum = scodeLineNum;
    }

    public Compare(long solvedId, String userId, String scodeId) {
        this.solvedId = solvedId;
        this.userId = userId;
        this.scodeId = scodeId;
    }

    public Compare(String userId, long secId) {
        this.userId = userId;
        this.secId = secId;
    }

}
