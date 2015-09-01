package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Date;
import java.util.Map;

public class MerUpdateProjectRequestModel extends BaseSyncRequestModel {
    private long projectId;
    private String projectName;
    private long projectAmount;
    private String changeType;
    private String projectState;
    private String projectExpireDate;


    public MerUpdateProjectRequestModel(long loanAmount, long projectId, String projectName,String changeType) {
        super();
        this.service = "mer_bind_project";
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectAmount = loanAmount;
        this.changeType = changeType;
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("service", this.service);
        payRequestData.put("project_id", String.valueOf(this.projectId));
        payRequestData.put("project_name", this.projectName);
        payRequestData.put("project_amount", String.valueOf(this.projectAmount));
//        payRequestData.put("loan_user_id", String.valueOf(this.loanUserId));
        return payRequestData;
    }

}
