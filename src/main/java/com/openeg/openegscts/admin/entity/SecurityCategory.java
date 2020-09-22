package com.openeg.openegscts.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCategory {
    private long scategoryId;
    private String scategoryName;
    private Date scRegDate;

    public SecurityCategory(String scategoryName) {
        this.scategoryName = scategoryName;
    }
}
