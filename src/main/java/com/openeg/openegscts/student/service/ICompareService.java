package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.CompareDto;
import com.openeg.openegscts.student.dto.CorrectResultDto;
import com.openeg.openegscts.student.dto.IncorrectResultDto;

public interface ICompareService {

    CompareDto getLineNum(String scodeId);
    CompareDto getKeyword(String scodeId);
    CorrectResultDto compareLineNum(CompareDto compareDto);
    CorrectResultDto compareSolution(CompareDto compareDto) throws InterruptedException;
    CorrectResultDto getCorrectResult(String scodeId, String userId);
    IncorrectResultDto getIncorrectResult(String scodeId, String userId);
    long insertSolvedCode(CompareDto compareDto);
}
