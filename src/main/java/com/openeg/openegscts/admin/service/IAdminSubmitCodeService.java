package com.openeg.openegscts.admin.service;

import com.openeg.openegscts.admin.dto.AdminRejectDto;
import com.openeg.openegscts.admin.entity.AdminSubmitCode;

import java.util.List;

public interface IAdminSubmitCodeService {

    List<AdminSubmitCode> getAllSubmitCodeList();
    List<AdminSubmitCode> getAllNewSubmit();
    int updateOk(String scodeId);
    int updateNotOk(AdminRejectDto adminRejectDto);
}
