package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.google.common.collect.Maps;
import com.tuotiansudai.utils.AmountUtil;

import java.math.BigDecimal;
import java.util.Map;

public class MerBindProjectRequestModel extends BaseSyncRequestModel{
    private long projectId;
    private String projectName;
    private long projectAmount;
    private long loanUserId;

    public MerBindProjectRequestModel(long loanUserId,long loanAmount,long projectId,String projectName){
        super();
        this.service = "mer_bind_project";
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectAmount = loanAmount;
        this.loanUserId = loanUserId;
    }
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("service", this.service);
        payRequestData.put("project_id", String.valueOf(this.projectId));
        payRequestData.put("project_name", this.projectName);
        payRequestData.put("project_amount", String.valueOf(this.projectAmount));
        payRequestData.put("loan_user_id", String.valueOf(this.loanUserId));
        return payRequestData;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(long projectAmount) {
        this.projectAmount = projectAmount;
    }

    public long getLoanUserId() {
        return loanUserId;
    }

    public void setLoanUserId(long loanUserId) {
        this.loanUserId = loanUserId;
    }
}
