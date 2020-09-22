package com.openeg.openegscts.student.model;

import lombok.Data;

@Data
public class LoginRequestModel {

    private String email;
    private String password;
    private int type;
    private String accessToken;
}
