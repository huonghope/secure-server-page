package com.openeg.openegscts.student.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitResult {

    private String scodeId;
    private String scodeVulDesc;
    private String scodeSecDesc;
    private int tryNumFirst;
    private int tryNumSecond;

}
