package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.Map;

public class MerUpdateProjectRequestModel extends BaseSyncRequestModel {
    private long projectId;
    private String projectName;
    private long projectAmount;
    private String changeType;
    private String projectState;
    private String projectExpireDate;


    public MerUpdateProjectRequestModel(long loanAmount, long projectId, String projectName, String projectState) {
        this.service = "mer_update_project";
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectAmount = loanAmount;
        this.changeType = "01"; //更新标的
        this.projectState = projectState;
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("service", this.service);
        payRequestData.put("project_id", String.valueOf(this.projectId));
        payRequestData.put("change_type", this.changeType);
        payRequestData.put("project_state", this.projectState);
        // 从筹款到还款的状态变化时，以下字段不能添加，否则会失败
        if (Lists.newArrayList(LoanStatus.PREHEAT.getCode(), LoanStatus.RAISING.getCode()).contains(this.projectState)) {
            payRequestData.put("project_name", this.projectName);
            payRequestData.put("project_amount", String.valueOf(this.projectAmount));
        }
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

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public String getProjectExpireDate() {
        return projectExpireDate;
    }

    public void setProjectExpireDate(String projectExpireDate) {
        this.projectExpireDate = projectExpireDate;
    }
}
