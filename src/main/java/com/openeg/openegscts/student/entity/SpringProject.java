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
public class SpringProject {
	
	// project table
	private String projectId;
	private String projectUserId;
	private String projectName;
	private String projectType;
	private String projectPath;
	private Date projectCreated;
	
	// spring project table
	private String projectSpringId;
	private String projectBuildType;
	private String projectLanguage;
	private String projectSpringBootVer;
	private String projectMetaGroup;
	private String projectMetaDesc;
	private String projectMetaPackage;
	
}
