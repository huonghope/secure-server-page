package com.openeg.openegscts.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCodeDto {

    private long secId;
    private String secName;
    private String secPdf;
    private String secVideo;
    private long scategoryId;

}
