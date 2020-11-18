package com.openeg.openegscts.student.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDiagnosisDto {
	
	 	private String userId;
	    private String projectId;
	    private String path;
	    private Date time;
	    
	    
	    @Builder
	    public ProjectDiagnosisDto(String userId, String projectId, String path, Date time)
	    {
	    	this.userId = userId;
	    	this.projectId = projectId;
	    	this.path = path;
	    	this.time = time;
	    }
}
