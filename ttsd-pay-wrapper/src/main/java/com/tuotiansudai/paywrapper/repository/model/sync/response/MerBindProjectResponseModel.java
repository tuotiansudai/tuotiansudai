package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

public class MerBindProjectResponseModel extends BaseSyncResponseModel {
    private String projectAccountId;
    private String projectState;
    private String merCheckDate;

    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.setProjectAccountId(resData.get("project_account_id"));
        this.setProjectState(resData.get("project_state"));
        this.setMerCheckDate(resData.get("mer_check_date"));
    }


    public String getProjectAccountId() {
        return projectAccountId;
    }

    public void setProjectAccountId(String projectAccountId) {
        this.projectAccountId = projectAccountId;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public String getMerCheckDate() {
        return merCheckDate;
    }

    public void setMerCheckDate(String merCheckDate) {
        this.merCheckDate = merCheckDate;
    }
}
