package com.openeg.openegscts.student.repository;

import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IUserMapper {

    int signUpUser(Users user);
    int signUpUserOAuth(Users user);
    int signUpTrainer(Users user);
    int signUpTrainerOAuth(Users user);
    Users findByEmail(String email);
    Users confirmUser(String email);
    Users confirmUserOauth(String email);
    Users confirmTrainer(String email);
    Users confirmTrainerOauth(String email);
    Users getUserById(String userId);
    int updateUser(UsersDto user);
    List<SolvedCode> getSolvedCode(String userId);

}