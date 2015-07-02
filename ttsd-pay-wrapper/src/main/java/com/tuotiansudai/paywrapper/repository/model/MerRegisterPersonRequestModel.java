package com.tuotiansudai.paywrapper.repository.model;

import java.util.Map;

public class MerRegisterPersonRequestModel extends BaseRequestModel {

    private Long id;

    private String loginName;

    private String userName;

    private String identityType = "IDENTITY_CARD";

    private String identityNumber;

    private String mobileNumber;

    public MerRegisterPersonRequestModel() {
    }

    public MerRegisterPersonRequestModel(String loginName, String userName, String identityNumber, String mobileNumber) {
        super();
        this.service = "mer_register_person";
        this.loginName = loginName;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.mobileNumber = mobileNumber;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("mer_cust_id", this.loginName);
        payRequestData.put("mer_cust_name", this.userName);
        payRequestData.put("identity_type", this.identityType);
        payRequestData.put("identity_code", this.identityNumber);
        payRequestData.put("mobile_id", this.mobileNumber);
        return payRequestData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MerRegisterPersonRequestModel that = (MerRegisterPersonRequestModel) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
