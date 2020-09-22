package com.openeg.openegscts.trainer.model;

import lombok.Data;

import java.util.Date;

@Data
public class GetSubmitCodeListModel {
    private String scodeId;
    private long secId;
    private String languageId;
    private int scodeApproval;
    private Date scodeRegDate;
    private String languageType;
    private String secName;
    private String scategoryName;
}
