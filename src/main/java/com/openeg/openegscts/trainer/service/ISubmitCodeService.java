package com.openeg.openegscts.trainer.service;


import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.trainer.dto.SubmitCodeDto;
import com.openeg.openegscts.trainer.entity.SubmitCode;

import java.util.List;

public interface ISubmitCodeService {

    SubmitCodeDto insertSubmitCode(SubmitCodeDto submitCodeDto);
    List<SubmitCode> getSVbyCategory(long scategoryId);
    SubmitCode getLastData();
    List<SubmitCode> getSubmitCodeList(String userId);
    SubmitCodeDto getSubmitCode(String scodeId);
    SubmitCodeDto updateSubmitCode(SubmitCodeDto submitCodeDto);
    String deleteSubmitCode(String scodeId);
    Users getUserEmail(String userId);

}