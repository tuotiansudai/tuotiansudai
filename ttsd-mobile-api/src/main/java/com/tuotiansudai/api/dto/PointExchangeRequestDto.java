package com.tuotiansudai.api.dto;

public class PointExchangeRequestDto extends BaseParamDto {
    private String couponId;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

}
