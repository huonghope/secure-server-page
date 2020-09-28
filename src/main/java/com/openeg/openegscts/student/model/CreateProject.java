package com.openeg.openegscts.student.model;

import lombok.Data;

@Data
public class CreateProject {
	private String projectUserId; 
    private String projectName;
    private String projectType;
}
