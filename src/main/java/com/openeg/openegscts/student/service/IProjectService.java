package com.openeg.openegscts.student.service;

import java.util.List;

import com.openeg.openegscts.student.dto.ProjectDto;
import com.openeg.openegscts.student.dto.SpringProjectDto;
import com.openeg.openegscts.student.entity.Project;

public interface IProjectService {
	
	ProjectDto createProject(ProjectDto project);
	SpringProjectDto createSpringProject(SpringProjectDto project);
	List<Project> getMyProjects(String userId);
}
