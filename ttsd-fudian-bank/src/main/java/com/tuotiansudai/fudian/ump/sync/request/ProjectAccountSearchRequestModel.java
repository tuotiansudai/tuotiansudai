package com.tuotiansudai.fudian.ump.sync.request;

import com.tuotiansudai.fudian.ump.SyncUmPayService;

import java.util.Map;

/**
 * Created by qduljs2011 on 2018/7/19.
 */
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
