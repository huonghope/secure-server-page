package com.openeg.openegscts.trainer.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitCodeDto {

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
    private int isOK;

    @Builder
    public SubmitCodeDto(String scodeId, long secId, String languageId, String scodeVulFile, String scodeVulDesc, String scodeSecFile, String scodeSecDesc, String scodeLineNum) {
        this.scodeId = scodeId;
        this.secId = secId;
        this.languageId = languageId;
        this.scodeVulFile = scodeVulFile;
        this.scodeVulDesc = scodeVulDesc;
        this.scodeSecFile = scodeSecFile;
        this.scodeSecDesc = scodeSecDesc;
        this.scodeLineNum = scodeLineNum;
    }

    public SubmitCodeDto(String scodeId, long secId, String languageId, String scodeVulFile, String scodeVulDesc, String scodeSecFile, String scodeSecDesc, String scodeLineNum, int scodeApproval) {
        this.scodeId = scodeId;
        this.secId = secId;
        this.languageId = languageId;
        this.scodeVulFile = scodeVulFile;
        this.scodeVulDesc = scodeVulDesc;
        this.scodeSecFile = scodeSecFile;
        this.scodeSecDesc = scodeSecDesc;
        this.scodeLineNum = scodeLineNum;
        this.scodeApproval = scodeApproval;
    }

    public SubmitCodeDto(int isOK) {
        this.isOK = isOK;
    }
}
