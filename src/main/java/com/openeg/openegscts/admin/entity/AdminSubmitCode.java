package com.openeg.openegscts.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSubmitCode {
    private String scodeId;
    private String name;
    private String secName;
    private String scategoryName;
    private String languageType;
    private long scodeTryNum;
    private long scodeSuccNum;
    private Date scodeRegDate;
}
