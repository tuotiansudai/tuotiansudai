package com.tuotiansudai.fudian.ump.sync.request;



import com.tuotiansudai.fudian.ump.SyncUmPayService;

import java.util.Map;

public class ResertPwdModel extends BaseSyncRequestModel {

    private String userId;

    private String identityCode;

    private String orderId;

    public ResertPwdModel() {
    }

    public ResertPwdModel(String userId, String identityCode, String orderId) {
        this.service = SyncUmPayService.MER_SEND_SMS_PWD.getServiceName();
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
