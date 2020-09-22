package com.openeg.openegscts.student.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeList {

    private long secId;
    private String scodeId;
    private String solvedId;
    private Date solvedDateFirst;
    private Date solvedDateSecond;
    private String languageId;
    private String name;
    private Date scodeRegDate;
    private Date lastTryDate;
}
