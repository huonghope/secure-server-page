package com.openeg.openegscts.student.entity;

import java.sql.Date;

import com.openeg.openegscts.student.dto.UsersDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
	
	private String projectId;
	private String projectUserId;
	private String projectName;
	private String projectType;
	private String projectPath;
	private Date projectCreated;
	
}
