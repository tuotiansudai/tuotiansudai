package com.tuotiansudai.api.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class UserCouponListResponseDataDto extends BaseResponseDataDto {

    private List<UserCouponResponseDataDto> coupons = Lists.newArrayList();

    public UserCouponListResponseDataDto() {
    }

    public UserCouponListResponseDataDto(List<UserCouponResponseDataDto> coupons) {
        this.coupons = coupons;
    }

    public List<UserCouponResponseDataDto> getCoupons() {
        return coupons;
    }
}
