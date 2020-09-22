package com.openeg.openegscts.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersListDto {
    private String userId;
    private String name;
    private String email;
    private int type;
    private Date regDate;
}
