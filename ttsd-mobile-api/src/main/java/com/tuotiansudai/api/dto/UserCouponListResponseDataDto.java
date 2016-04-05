package com.tuotiansudai.api.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class UserCouponListResponseDataDto extends BaseResponseDataDto {

    private List<BaseCouponResponseDataDto> coupons = Lists.newArrayList();

    public UserCouponListResponseDataDto() {
    }

    public UserCouponListResponseDataDto(List<BaseCouponResponseDataDto> coupons) {
        this.coupons = coupons;
    }



    public List<BaseCouponResponseDataDto> getCoupons() {
        return coupons;
    }
}
