package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.tuotiansudai.enums.SyncUmPayService;

import java.util.Map;

public class MerUpdateProjectRequestModel extends BaseSyncRequestModel {
    private String projectId;
    private String projectName;
    private String projectAmount;
    private String changeType;
    private String projectState;
    private String projectExpireDate;


    public MerUpdateProjectRequestModel(String projectId, String projectState) {
        this.service = SyncUmPayService.MER_UPDATE_PROJECT.getServiceName();
        this.projectId = projectId;
        this.changeType = "01"; //更新标的
        this.projectState = projectState;
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("service", this.service);
        payRequestData.put("project_id", String.valueOf(this.projectId));
        payRequestData.put("change_type", this.changeType);
        payRequestData.put("project_state", this.projectState);
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
