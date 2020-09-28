package com.openeg.openegscts.student.model;

import lombok.Data;

@Data
public class CreateSpringProject {
	private String projectUserId; 
    private String projectName;
    private String projectType;
    private String projectBuildType; 
    private String projectLanguage;
    private String projectSpringBootVer;
    private String projectMetaGroup;
    private String projectMetaDesc;
    private String projectMetaPackage;
}
