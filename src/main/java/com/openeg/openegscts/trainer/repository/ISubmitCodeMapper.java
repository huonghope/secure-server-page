package com.openeg.openegscts.trainer.repository;

import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.trainer.dto.SubmitCodeDto;
import com.openeg.openegscts.trainer.entity.SubmitCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ISubmitCodeMapper {

    int insertSubmitCode(SubmitCode submitCode);
    List<SubmitCode> getSVbyCategory(long scategoryId);
    SubmitCode getLastData();
    List<SubmitCode> getSubmitCodeList(String userId);
    SubmitCode getSubmitCode(String scodeId);
    int updateSubmitCode(SubmitCodeDto submitCodeDto);
    int deleteSubmitCode(String scodeId);
    Users getUserEmail(String userId);


}