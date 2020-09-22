package com.openeg.openegscts.admin.service;

import com.openeg.openegscts.admin.dto.SecurityCategoryDto;
import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.admin.entity.SecurityCategory;
import com.openeg.openegscts.student.entity.SecurityCode;

import java.util.List;

public interface ISVService {

    SecurityCodeDto insertSV(SecurityCodeDto securityCodeDto);
    SecurityCategoryDto insertSC(SecurityCategoryDto securityCategoryDto);
    List<SecurityCategory> getAllSecurityCategoryInfo();
    List<SecurityCode> getAllSV();
    int deleteSV(long secId);
    int confirmSecName(String secName);
    int deleteSCategory(long scategoryId);

}
