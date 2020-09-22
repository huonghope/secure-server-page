package com.openeg.openegscts.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectResultDto {

    private String scodeId;
    private String scodeVulDesc;
    private String scodeSecDesc;

    private int tryNumFirst;
    private int tryNumSecond;

    private int isError;
    private Date lastTryDate;

    public CorrectResultDto(String scodeId, int tryNumFirst, int tryNumSecond) {
        this.scodeId = scodeId;
        this.tryNumFirst = tryNumFirst;
        this.tryNumSecond = tryNumSecond;
    }

    public CorrectResultDto(int isError) {
        this.isError = isError;
    }
}
