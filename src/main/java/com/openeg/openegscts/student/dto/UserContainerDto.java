
package com.openeg.openegscts.student.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContainerDto {

	private String containerId;
	private String projectId;
	private String userId;
	private String containerName;
	private int vscodePort;
	private int nodePort;
	private int javaPort;
	private int pythonPort;
	private int state;
	private Date containerCreated;
	
	
	 @Builder 
	 public UserContainerDto(String containerId, String projectId, String userId, String containerName, int vscodePort, int nodePort, int javaPort, int pythonPort, int state) 
	 {
	      this.containerId = containerId;
	      this.projectId = projectId;
	      this.userId = userId;
	      this.containerName = containerName;
	      this.vscodePort = vscodePort;
	      this.nodePort = nodePort;
	      this.javaPort = javaPort;
	      this.pythonPort = pythonPort;
	      this.state = state;
	 }
	 
	 @Builder 
	 public UserContainerDto(String projectId, String userId, String containerName, int vscodePort, int nodePort, int javaPort, int pythonPort, int state) 
	 {
	      this.projectId = projectId;
	      this.userId = userId;
	      this.containerName = containerName;
	      this.vscodePort = vscodePort;
	      this.nodePort = nodePort;
	      this.javaPort = javaPort;
	      this.pythonPort = pythonPort;
	      this.state = state;
	 }
	 
}
