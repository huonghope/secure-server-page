package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.student.dto.SolvedCodeDto;
import com.openeg.openegscts.student.entity.CodeList;
import com.openeg.openegscts.student.entity.Compare;

import java.util.List;

public interface ICodeInfoService {

    SecurityCodeDto getSecurityInfo(long secId);
    SolvedCodeDto getStepInfo(String scodeId, String userId);
    List<CodeList> getCodeListBySecId(Compare compare);

}
