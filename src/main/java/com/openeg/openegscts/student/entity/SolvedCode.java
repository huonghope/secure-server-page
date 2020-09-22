package com.openeg.openegscts.student.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SolvedCode {

    private long solvedId;
    private String scodeId;
    private String userId;
    private int tryNumFirst;
    private int tryNumSecond;
    private Date solvedDateFirst;
    private Date solvedDateSecond;
    private String languageType;
    private String secName;

    public SolvedCode(long solvedId, String userId, String scodeId, int tryNumFirst, int tryNumSecond, Date solvedDateFirst, Date solvedDateSecond) {
        this.solvedId = solvedId;
        this.userId = userId;
        this.scodeId = scodeId;
        this.tryNumFirst = tryNumFirst;
        this.tryNumSecond = tryNumSecond;
        this.solvedDateFirst = solvedDateFirst;
        this.solvedDateSecond = solvedDateSecond;
    }
}
