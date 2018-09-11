package com.tuotiansudai.fudian.ump.sync.request;


import com.tuotiansudai.fudian.ump.SyncUmPayService;

import java.util.Map;

public class PtpMerQueryRequestModel extends BaseSyncRequestModel {

    private String queryMerId;

    private String accountType;

    public PtpMerQueryRequestModel() {
        this.service = SyncUmPayService.PTP_MER_QUERY.getServiceName();
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
