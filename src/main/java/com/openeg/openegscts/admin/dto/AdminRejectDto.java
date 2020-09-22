package com.openeg.openegscts.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRejectDto {
    private int rejectReason;
    private String scodeId;
}
