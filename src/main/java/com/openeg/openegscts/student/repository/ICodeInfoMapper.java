package com.openeg.openegscts.student.repository;

import com.openeg.openegscts.student.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ICodeInfoMapper {

    SecurityCode getSecurityInfo(long secId);
    SolvedCode getStepInfo(String scodeId, String userId);
    List<CodeList> getCodeListBySecId(Compare compare);
}
