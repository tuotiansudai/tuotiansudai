package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.tuotiansudai.enums.SyncUmPayService;

import java.util.Map;

public class ProjectAccountSearchRequestModel extends BaseSyncRequestModel {

    private String projectId;

    public ProjectAccountSearchRequestModel() {
    }

    public ProjectAccountSearchRequestModel(String projectId) {
        this.service = SyncUmPayService.PROJECT_ACCOUNT_SEARCH.getServiceName();
        this.projectId = projectId;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("project_id", this.projectId);
        return payRequestData;
    }
}
