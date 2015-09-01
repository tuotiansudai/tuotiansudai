package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

public class MerUpdateProjectResponseModel extends BaseSyncResponseModel{
    private String projectState;
    private String merCheckDate;

    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.setProjectState(resData.get("project_state"));
        this.setMerCheckDate(resData.get("mer_check_date"));
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
