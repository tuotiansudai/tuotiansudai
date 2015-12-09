package com.tuotiansudai.coupon.service;


import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;

public interface CouponService {

    BaseDto<PayDataDto> createCoupon(String loginName,CouponDto couponDto);
}
