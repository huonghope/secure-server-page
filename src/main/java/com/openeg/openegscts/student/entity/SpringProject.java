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
	private Date projectCreated;
	
	// spring project table
	private String projectSpringId;
	private String projectBuildType;
	private String projectLanguage;
	private String projectSpringBootVer;
	private String projectMetaGroup;
	private String projectMetaDesc;
	private String projectMetaPackage;
	
//	@Builder
//    public Project(String projectUserId, String projectId, String projectName, String projectType, Date projectCreated) {
//        this.projectUserId = projectUserId;
//        this.projectId = projectId;
//        this.projectName = projectName;
//        this.projectType = projectType;
//        this.projectCreated = projectCreated;
//    }
}
