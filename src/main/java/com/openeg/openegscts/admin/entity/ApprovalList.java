package com.openeg.openegscts.admin.entity;

import com.openeg.openegscts.admin.dto.ApprovalListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalList {
    private List<ApprovalListDto> scodeIdList;
}
