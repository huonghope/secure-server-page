package com.openeg.openegscts.admin.service;

import com.openeg.openegscts.admin.dto.AdminRejectDto;
import com.openeg.openegscts.admin.entity.AdminSubmitCode;
import com.openeg.openegscts.admin.repository.IAdminSubmitCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminSubmitCodeServiceImpl implements IAdminSubmitCodeService {

    IAdminSubmitCodeMapper iAdminSubmitCodeMapper;

    @Autowired
    public AdminSubmitCodeServiceImpl(IAdminSubmitCodeMapper iAdminSubmitCodeMapper) {
        this.iAdminSubmitCodeMapper = iAdminSubmitCodeMapper;
    }

    @Override
    public List<AdminSubmitCode> getAllSubmitCodeList() {
        List<AdminSubmitCode> scList = iAdminSubmitCodeMapper.getAllSubmitCodeList();

        if(scList == null) {
            log.info(String.format("not exists submit code list"));
            return new ArrayList<>();
        }

        return scList;
    }

    @Override
    public List<AdminSubmitCode> getAllNewSubmit() {
        List<AdminSubmitCode> newList = iAdminSubmitCodeMapper.getAllNewSubmit();

        if(newList == null) {
            log.info(String.format("not exists new submit code list"));
            return new ArrayList<>();
        }

        return newList;
    }

    @Override
    public int updateOk(String scodeId) {
        int result = iAdminSubmitCodeMapper.updateOk(scodeId);
        return result;
    }

    @Override
    public int updateNotOk(AdminRejectDto adminRejectDto) {
        int result = iAdminSubmitCodeMapper.updateNotOk(adminRejectDto);
        return result;
    }
}
