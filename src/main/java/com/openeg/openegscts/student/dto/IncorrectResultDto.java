package com.openeg.openegscts.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncorrectResultDto {

    private String scodeId;
    private int tryNumFirst;
    private int tryNumSecond;

}
