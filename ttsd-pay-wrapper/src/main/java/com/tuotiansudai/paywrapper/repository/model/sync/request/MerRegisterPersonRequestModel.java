package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Map;

public class MerRegisterPersonRequestModel extends BaseSyncRequestModel {

    private String orderId;

    private String loginName;

    private String userName;

    private String identityType = "IDENTITY_CARD";

    private String identityNumber;

    private String mobile;

    public MerRegisterPersonRequestModel() {
    }

    public MerRegisterPersonRequestModel(String orderId, String loginName, String userName, String identityNumber, String mobile) {
        super();
        this.service = "mer_register_person";
        this.orderId = orderId;
        this.loginName = loginName;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.mobile = mobile;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_cust_id", this.loginName);
        payRequestData.put("mer_cust_name", this.userName);
        payRequestData.put("identity_type", this.identityType);
        payRequestData.put("identity_code", this.identityNumber);
        payRequestData.put("mobile_id", this.mobile);
        return payRequestData;
    }


}
