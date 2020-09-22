package com.openeg.openegscts.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSubmitCodeDto {
    private String scodeId;
    private String name;
    private String secName;
    private String scategoryName;
    private String languageType;
    private Date scodeRegDate;
}
