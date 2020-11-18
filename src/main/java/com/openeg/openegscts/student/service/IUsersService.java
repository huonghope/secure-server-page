package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.ProjectDiagnosis;
import com.openeg.openegscts.student.entity.SolvedCode;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUsersService extends UserDetailsService {

    UsersDto signUpUser(UsersDto usersDto);
    UsersDto signUpUserOAuth(UsersDto usersDto);
    UsersDto signUpTrainer(UsersDto usersDto);
    UsersDto signUpTrainerOAuth(UsersDto usersDto);
    UsersDto getUserDetailsByEmail(String email);
    UsersDto confirmUser(String email, String encryptedPassword);
    UsersDto confirmUserOauth(String email);
    UsersDto confirmTrainer(String email, String encryptedPassword);
    UsersDto confirmTrainerOauth(String email);
    UsersDto getUserById(String userId);
    int confirmPwd(String userId, String Password);
    UsersDto updateUser(UsersDto usersDto);
    List<SolvedCode> getSolvedCode(String userId);
    
    boolean deleteProject(String projectId);
    boolean insertHistoryDiagnosis(String projectId, String userId, String path);
    List<ProjectDiagnosis> getProjectDiagnosis(String projectId);
}