package com.openeg.openegscts.student.model;

import lombok.Data;

@Data
public class GetCodeInfoModel {

    private String scodeId;
    private int scodeNum;
    private String scodeVulFile;
    private String scodeVulCode;
    private String scodeSecFile;
    private String scodeSecCode;
    private String scodeLineNum;

}
