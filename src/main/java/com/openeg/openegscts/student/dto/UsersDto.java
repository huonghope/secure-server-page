package com.openeg.openegscts.student.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
public class UsersDto {

    private String userId;
    private String name;
    private String email;
    private String password;
    private int type;
    private Date regDate;
    private int isOauth;

    @Builder
    public UsersDto(String userId, String name, String email, String password, int type, Date regDate) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.regDate = regDate;
    }

}
