package com.openeg.openegscts.student.dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpringProjectDto {
	private String projectId;
	private String projectUserId;
	private String projectName;
	private String projectType;
	
	private String projectSpringId;
	private String projectBuildType;
	private String projectLanguage;
	private String projectSpringBootVer;
	private String projectMetaGroup;
	private String projectMetaDesc;
	private String projectMetaPackage;
	
	@Builder
    public SpringProjectDto(String projectId,String projectUserId,  String projectName, String projectType,
    		String projectSpringId, String projectBuildType, String projectLanguage, String projectSpringBootVer, String projectMetaGroup, String projectMetaDesc, String projectMetaPackage) {
		this.projectId = projectId;
		this.projectUserId = projectUserId;
		this.projectName = projectName;
		this.projectType = projectType;
		
		this.projectSpringId = projectSpringId;
		this.projectBuildType = projectBuildType;
        this.projectLanguage = projectLanguage;
        this.projectSpringBootVer = projectSpringBootVer;
        this.projectMetaGroup = projectMetaGroup;
        this.projectMetaDesc = projectMetaDesc;
        this.projectMetaPackage = projectMetaPackage;
    }
}
