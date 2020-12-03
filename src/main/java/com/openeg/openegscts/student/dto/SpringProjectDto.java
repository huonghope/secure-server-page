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
	private String projectPath;
	
	private String projectSpringId;
	private String projectBuildType;
	private String projectLanguage;
	private String projectSpringBootVer;
	private String projectMetaGroupId;
	private String projectMetaArtifactId;
	private String projectMetaDesc;
	private String projectMetaPackage;
	private String projectDependencies;
	
	@Builder
    public SpringProjectDto(String projectId,String projectUserId,  String projectName, String projectType, String projectPath,
    		String projectSpringId, String projectBuildType, String projectLanguage, String projectSpringBootVer, String projectMetaGroupId, String projectMetaArtifactId, String projectMetaDesc, String projectMetaPackage
    		,String projectDependencies) 
	{
		this.projectId = projectId;
		this.projectUserId = projectUserId;
		this.projectName = projectName;
		this.projectType = projectType;
		this.projectPath = projectPath;
		
		this.projectSpringId = projectSpringId;
		this.projectBuildType = projectBuildType;
        this.projectLanguage = projectLanguage;
        this.projectSpringBootVer = projectSpringBootVer;
        this.projectMetaGroupId = projectMetaGroupId;
        this.projectMetaArtifactId = projectMetaArtifactId;
        this.projectMetaDesc = projectMetaDesc;
        this.projectMetaPackage = projectMetaPackage;
        this.projectDependencies = projectDependencies;
    }
}
