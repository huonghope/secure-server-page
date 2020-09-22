package com.openeg.openegscts.trainer.model;

import lombok.Data;

import java.util.Date;

@Data
public class GetSubmitCodeModel {
    private String scodeId;
    private String userId;
    private String scodeVulFile;
    private String scodeVulDesc;
    private String scodeVulCode;
    private String scodeSecFile;
    private String scodeSecDesc;
    private String scodeSecCode;
    private String scodeKeyword;
    private String scodeLineNum;
    private int scodeApproval;
    private Date scodeRegDate;
    private int scodeTryNum;
    private int scodeSuccNum;
    private String languageId;
    private String languageType;
    private long secId;
    private String secName;
    private int scategoryId;
    private String scategoryName;
    private int rejectReason;
}
