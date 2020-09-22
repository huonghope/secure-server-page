package com.openeg.openegscts.student.model;

import lombok.Data;

import java.sql.Date;

@Data
public class UserInfoResponseModel {

    private String userId;
    private String name;
    private String email;
    private int type;
    private Date regDate;
    private int isOauth;
}