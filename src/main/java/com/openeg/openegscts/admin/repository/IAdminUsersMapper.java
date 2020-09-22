package com.openeg.openegscts.admin.repository;

import com.openeg.openegscts.admin.dto.UsersListDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IAdminUsersMapper {

    List<UsersListDto> getUsersForAdmin();
    int deleteUser(String userId);

}