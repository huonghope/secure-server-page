package com.openeg.openegscts.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCategoryDto {
    private long scategoryId;
    private String scategoryName;
}