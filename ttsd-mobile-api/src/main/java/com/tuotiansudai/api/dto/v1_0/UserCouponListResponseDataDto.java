package com.tuotiansudai.api.dto.v1_0;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserCouponListResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "优惠券列表", example = "list")
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
