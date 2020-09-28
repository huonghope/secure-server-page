package com.openeg.openegscts.student.dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {
	private String projectUserId;
	private String projectName;
	private String projectType;
	private String projectId;
	private Date projectCreated;
	
	@Builder
    public ProjectDto(String projectUserId, String projectId, String projectName, String projectType) {
		this.projectUserId = projectUserId;
		this.projectId = projectId;
        this.projectName = projectName;
        this.projectType = projectType;
    }
}
