package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Map;

public class MerBindProjectRequestModel extends BaseSyncRequestModel {

    private String projectId;
    private String projectName;
    private String projectAmount;
    private String loanUserId;
    private String ctrlOverInvest; // 0: 不允许超投 1: 允许超投


    public MerBindProjectRequestModel(String loanUserId, String loanAmount, String projectId, String projectName) {
        super();
        this.service = "mer_bind_project";
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectAmount = loanAmount;
        this.loanUserId = loanUserId;
        this.ctrlOverInvest = "0";
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("service", this.service);
        payRequestData.put("project_id", this.projectId);
        payRequestData.put("project_name", this.projectName);
        payRequestData.put("project_amount", this.projectAmount);
        payRequestData.put("loan_user_id", this.loanUserId);
        payRequestData.put("ctrl_over_invest", this.ctrlOverInvest);
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

    public String getCtrlOverInvest() {
        return ctrlOverInvest;
    }

    public void setCtrlOverInvest(String ctrlOverInvest) {
        this.ctrlOverInvest = ctrlOverInvest;
    }
}
