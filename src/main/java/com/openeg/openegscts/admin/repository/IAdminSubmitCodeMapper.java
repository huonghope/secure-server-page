package com.openeg.openegscts.admin.repository;

import com.openeg.openegscts.admin.dto.AdminRejectDto;
import com.openeg.openegscts.admin.entity.AdminSubmitCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IAdminSubmitCodeMapper {

    List<AdminSubmitCode> getAllSubmitCodeList();
    List<AdminSubmitCode> getAllNewSubmit();
    int updateOk(String scodeId);
    int updateNotOk(AdminRejectDto adminRejectDto);
}
