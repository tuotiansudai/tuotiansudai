package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Map;

public class PtpMerQueryRequestModel extends BaseSyncRequestModel {

    private String queryMerId;

    private String accountType;

    public PtpMerQueryRequestModel() {
        super();
        this.service = "ptp_mer_query";
        this.queryMerId = UMP_PROPS.getProperty("mer_id");
        this.accountType = "01"; //现金账户
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("query_mer_id", this.queryMerId);
        payRequestData.put("account_type", this.accountType);
        return payRequestData;
    }
}
