package com.tuotiansudai.paywrapper.repository.model.sync.request;


import java.util.Map;

public class MerSendSmsPwdRequestModel extends BaseSyncRequestModel {

    private String userId;

    private String identityCode;

    private String orderId;

    public MerSendSmsPwdRequestModel() {
    }

    public MerSendSmsPwdRequestModel(String userId, String identityCode, String orderId) {
        this.service = "mer_send_sms_pwd";
        this.userId = userId;
        this.identityCode = identityCode;
        this.orderId = orderId;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("user_id", this.userId);
        payRequestData.put("identity_code", this.identityCode);
        payRequestData.put("order_id", this.orderId);
        return payRequestData;
    }
}
