package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Date;

/**
 * Created by tuotian on 15/8/17.
 */
public class MerBindProjectRequestModel extends BaseSyncRequestModel{
    private String sign;
    private String projectId;
    private String projectName;
    private String projectAmount;
    private String loanUserId;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(String projectAmount) {
        this.projectAmount = projectAmount;
    }

    public String getLoanUserId() {
        return loanUserId;
    }

    public void setLoanUserId(String loanUserId) {
        this.loanUserId = loanUserId;
    }
}
