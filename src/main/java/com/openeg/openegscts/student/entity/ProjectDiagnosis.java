package com.openeg.openegscts.student.entity;

import java.sql.Date;
import lombok.Builder;
import lombok.Data;

@Data
public class ProjectDiagnosis {
 	private String userId;
    private String projectId;
    private String path;
    private Date time;
    
    private String result;
    
    @Builder
    public ProjectDiagnosis(String userId, String projectId, String path, Date time, String result) {
        this.userId = userId;
        this.projectId = projectId;
        this.path = path;
        this.time = time;
        this.result = result;
    }
}


