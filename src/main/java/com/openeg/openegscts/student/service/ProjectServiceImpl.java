package com.openeg.openegscts.student.service;

import java.io.File;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openeg.openegscts.student.dto.ProjectDto;
import com.openeg.openegscts.student.dto.SpringProjectDto;
import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.Project;
import com.openeg.openegscts.student.entity.SpringProject;
import com.openeg.openegscts.student.repository.IUserMapper;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectServiceImpl implements IProjectService{

	ModelMapper modelMapper = new ModelMapper();
	IUserMapper userMapper;
	
	@Autowired
	public ProjectServiceImpl(IUserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	@Override
	public ProjectDto createProject(ProjectDto projectDto) {
		//프로젝트 경로 == 프로젝트 이름
		projectDto = ProjectDto.builder()
					.projectId(UUID.randomUUID().toString())
					.projectUserId(projectDto.getProjectUserId())
	                .projectName(projectDto.getProjectName())
	                .projectPath(projectDto.getProjectName())
	                .projectType(projectDto.getProjectType())
	                .build();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        
        Project projectEntity = modelMapper.map(projectDto, Project.class);
        userMapper.insertProject(projectEntity);

        ProjectDto returnValue = modelMapper.map(projectEntity, ProjectDto.class);
        return returnValue;
	}
	
	@Override
	public List<Project> getMyProjects(String userId) {
		try {
			List<Project> listMyProjects = userMapper.getMyProjects(userId);
			return listMyProjects;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	@Override
    public boolean deleteProject(String projectId) {		
    	boolean result = userMapper.deleteProject(projectId);
		return result;
    }
    @Override
    public boolean insertHistoryDiagnosis(String projectId, String userId, String path) {
    	boolean result = userMapper.insertHistoryDiagnosis(projectId, userId, path);
		return result;
    }
    
	@Override
	public Project getProjectById(String projectId) {
		try {
			Project project = userMapper.getProjectById(projectId);
			return project;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	@Override
	public SpringProjectDto createSpringProject(SpringProjectDto springProjectDto) {
		springProjectDto = SpringProjectDto.builder()
				.projectId(UUID.randomUUID().toString())   // project table ID
				.projectUserId(springProjectDto.getProjectUserId())
				.projectName(springProjectDto.getProjectName())
				.projectType(springProjectDto.getProjectType())
				.projectPath(springProjectDto.getProjectName())
				
				.projectSpringId(UUID.randomUUID().toString()) // spring project table ID
	
				.projectBuildType(springProjectDto.getProjectBuildType())
				.projectLanguage(springProjectDto.getProjectLanguage())
				.projectSpringBootVer(springProjectDto.getProjectSpringBootVer())
				.projectMetaGroup(springProjectDto.getProjectMetaGroup())
				.projectMetaDesc(springProjectDto.getProjectMetaDesc())
				.projectMetaPackage(springProjectDto.getProjectMetaPackage())
				.build();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
		SpringProject projectEntity = modelMapper.map(springProjectDto, SpringProject.class);
	    userMapper.insertSpringProject(projectEntity);
	
	    SpringProjectDto returnValue = modelMapper.map(projectEntity, SpringProjectDto.class);
	    return returnValue;
	}
	
	@Override
	public boolean checkExistsProjectName(String projectName) {
		List<Project> result = userMapper.checkProjectName(projectName);
		if(result.size() == 0) {
			return false;
		}
		return true;
	}
}
