package com.openeg.openegscts.student.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertSolvedCode {

    private long solvedId;
    private String userId;
    private String scodeId;

}
