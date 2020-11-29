package com.openeg.openegscts.student.repository;

import com.openeg.openegscts.student.dto.ContainerDto;
import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.Container;
import com.openeg.openegscts.student.entity.OwaspContainer;
import com.openeg.openegscts.student.entity.Project;
import com.openeg.openegscts.student.entity.ProjectDiagnosis;
import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.SpringProject;
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

    int insertProject(Project project);
    int insertSpringProject(SpringProject project);
    List<Project> getMyProjects(String userId);
    
    boolean deleteProject(String projectId);
    boolean insertHistoryDiagnosis(String projectId, String userId, String path);
    List<Project> checkProjectName(String project);
    Project getProjectById(String projectId);
    List<ProjectDiagnosis> getProjectDiagnosis(String userId);

    void insertContainer(Container container);
    Container getUserContainer(String userId);
    void stopContainer(String containerName);
    void startContainer(String containerName);
    void updateContainerForProjectId(String projectId, String containerName);
    
    OwaspContainer getUserOwaspContainer(String containerName);
    void insertOwaspContainer(OwaspContainer owaspcontainer);
}