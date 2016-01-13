package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Map;

public class TransferSearchRequestModel extends BaseSyncRequestModel {

    private String orderId;

    private String merDate;

    private String busiType; //01充值 02提现 03标的转账 04转账

    public TransferSearchRequestModel() {
    }

    public TransferSearchRequestModel(String orderId, String merDate, String busiType) {
        super();
        this.service = "transfer_search";
        this.orderId = orderId;
        this.merDate = merDate;
        this.busiType = busiType;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("busi_type", this.busiType);
        return payRequestData;
    }
}
