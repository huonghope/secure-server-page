package com.openeg.openegscts.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolvedCodeDto {

    private long solvedId;
    private String scodeId;
    private int tryNumFirst;
    private int tryNumSecond;
    private Date solvedDateFirst;
    private Date solvedDateSecond;

}
