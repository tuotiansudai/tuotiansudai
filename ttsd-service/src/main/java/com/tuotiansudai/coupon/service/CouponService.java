package com.tuotiansudai.coupon.service;


import com.tuotiansudai.coupon.dto.CouponDto;

public interface CouponService {

    boolean createCoupon(String loginName,CouponDto couponDto);
}
