package com.openeg.openegscts.student.entity;

import lombok.*;

import java.util.Date;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private String userId;
    private String name;
    private String email;
    private String password;
    private int type;
    private Date regDate;
    private int isOauth;

    @Builder
    public Users(String userId, String name, String email, String password, Date regDate) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.regDate = regDate;
    }

    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Users(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
