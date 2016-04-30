package com.tuotiansudai.api.dto;

public class ReferrerListRequestDto extends BaseParamDto{
    private String referrerId;

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }
}
