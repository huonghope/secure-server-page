package com.openeg.openegscts.student.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwaspContainerDto {
	private String containerId;
	private String userId;
	private String containerName;
	private int containerPort;
	private Date containerCreated;
	
	@Builder 
	 public OwaspContainerDto(String containerId, String userId, String containerName, int containerPort) 
	 {
	      this.containerId = containerId;
	      this.userId = userId;
	      this.containerName = containerName;
	      this.containerPort = containerPort;
	 }
	 @Builder 
	 public OwaspContainerDto(String userId, String containerName, int containerPort) 
	 {
	      this.userId = userId;
	      this.containerName = containerName;
	      this.containerPort = containerPort;
	 }
}
