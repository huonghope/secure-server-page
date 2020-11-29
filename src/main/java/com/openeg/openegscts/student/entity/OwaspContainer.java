package com.openeg.openegscts.student.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwaspContainer {
	private String containerId;
	private String userId;
	private int containerPort;
	private String containerName;
	private Date containerCreated;

}
