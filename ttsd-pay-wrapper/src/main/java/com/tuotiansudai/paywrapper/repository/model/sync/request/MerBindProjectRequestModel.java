package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Map;

public class MerBindProjectRequestModel extends BaseSyncRequestModel{
    private String projectId;
    private String projectName;
    private String projectAmount;
    private String loanUserId;

    public MerBindProjectRequestModel(String loanUserId,String loanAmount,String projectId,String projectName){
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
