package com.openeg.openegscts.student.repository;

import com.openeg.openegscts.student.entity.Compare;
import com.openeg.openegscts.student.entity.InsertSolvedCode;
import com.openeg.openegscts.student.entity.SubmitResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ICompareMapper {

    Compare getLineNum(String scodeId);
    Compare getKeyword(String scodeId);
    SubmitResult getSubmitResult(String scodeId, String userId);
    int insertSolvedCode(Compare compare);
    InsertSolvedCode getInsertSolvedCode(String scodeId, String userId);
    int updateLastTryDate(long solvedId);
    int updateLastTryDate2(String scodeId, String userId);
    int updateFirstTryNum(long solvedId);
    int updateSecondTryNum(String scodeId, String userId);
    int updateFirstSolvedDate(long solvedId);
    int updateSecondSolvedDate(String scodeId, String userId);
    int updateTotalTryNum(String scodeId);
    int updateTotalSuccNum(String scodeId);

}
