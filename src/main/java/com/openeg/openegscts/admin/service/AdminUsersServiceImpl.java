package com.openeg.openegscts.admin.service;

import com.openeg.openegscts.admin.dto.UsersListDto;
import com.openeg.openegscts.admin.repository.IAdminUsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminUsersServiceImpl implements IAdminUsersService {

    IAdminUsersMapper iAdminUsersMapper;

    @Autowired
    public AdminUsersServiceImpl(IAdminUsersMapper iAdminUsersMapper) {
        this.iAdminUsersMapper = iAdminUsersMapper;
    }

    @Override
    public List<UsersListDto> getUsersForAdmin() {
        List<UsersListDto> usersList = iAdminUsersMapper.getUsersForAdmin();

        if(usersList == null) {
            log.info(String.format("not exists user list"));
            return new ArrayList<>();
        }

        return usersList;
    }

    @Override
    public int deleteUser(String userId) {
        int deleteUser = iAdminUsersMapper.deleteUser(userId);
        return deleteUser;
    }
}