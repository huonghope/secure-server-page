package com.openeg.openegscts.student.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCode {

    private long secId;
    private String secName;
    private String secPdf;
    private String secVideo;
    private Date secRegDate;
    private long scategoryId;
    private String scategoryName;

}
