package com.openeg.openegscts.admin.service;


import com.openeg.openegscts.admin.dto.UsersListDto;

import java.util.List;

public interface IAdminUsersService {

    List<UsersListDto> getUsersForAdmin();
    int deleteUser(String userId);

}