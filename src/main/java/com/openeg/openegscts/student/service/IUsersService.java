package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.dto.ContainerDto;
import com.openeg.openegscts.student.dto.OwaspContainerDto;
import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.Container;
import com.openeg.openegscts.student.entity.OwaspContainer;
import com.openeg.openegscts.student.entity.ProjectDiagnosis;
import com.openeg.openegscts.student.entity.SolvedCode;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
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
    
    
    List<ProjectDiagnosis> getProjectDiagnosis(String projectId);
    
    Container getUserContainer(String userId);
	ContainerDto insertUserContainer(ContainerDto containerDto);
	boolean stopContainer(Container container) throws IOException, InterruptedException;
	boolean startContainer(Container container)  throws IOException, InterruptedException;
	boolean updateContainerForProjectId(String projectId, String containerName);
	
	OwaspContainer getUserOwaspContainer(String containerName);
	OwaspContainerDto insertUserOwaspContainer(OwaspContainerDto owaspcontainerDto);
	
}