package com.openeg.openegscts.student.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Container {
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
}
