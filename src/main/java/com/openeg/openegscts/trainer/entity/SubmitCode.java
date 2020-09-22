package com.openeg.openegscts.trainer.entity;

import lombok.*;

import java.util.Date;

@Data
@Getter
@NoArgsConstructor
public class SubmitCode {

    private String scodeId;
    private int scodeNum;
    private String userId;
    private long secId;
    private String languageId;
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
    private int rejectReason;
    private String languageType;
    private String secName;
    private String scategoryName;
    private int scategoryId;

    @Builder
    public SubmitCode(String scodeId, String userId, long secId, String languageId, String scodeVulFile, String scodeVulDesc, String scodeSecFile, String scodeSecDesc, String scodeLineNum, int scodeApproval, Date scodeRegDate, int scodeTryNum, int scodeSuccNum) {
        this.scodeId = scodeId;
        this.userId = userId;
        this.secId = secId;
        this.languageId = languageId;
        this.scodeVulFile = scodeVulFile;
        this.scodeVulDesc = scodeVulDesc;
        this.scodeSecFile = scodeSecFile;
        this.scodeSecDesc = scodeSecDesc;
        this.scodeLineNum = scodeLineNum;
        this.scodeApproval = scodeApproval;
        this.scodeRegDate = scodeRegDate;
        this.scodeTryNum = scodeTryNum;
        this.scodeSuccNum = scodeSuccNum;
    }

    public SubmitCode(String scodeId, long secId, String languageId, int scodeApproval, Date scodeRegDate, String languageType, String secName) {
        this.scodeId = scodeId;
        this.secId = secId;
        this.languageId = languageId;
        this.scodeApproval = scodeApproval;
        this.scodeRegDate = scodeRegDate;
        this.languageType = languageType;
        this.secName = secName;
    }

    public SubmitCode(int scodeNum) {
        this.scodeNum = scodeNum;
    }

    public SubmitCode(String secName, long secId) {
        this.secName = secName;
        this.secId = secId;
    }
}
