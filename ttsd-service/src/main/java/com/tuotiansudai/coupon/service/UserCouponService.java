package com.tuotiansudai.coupon.service;


public interface UserCouponService {

    long findExpectedTotalByCouponId(long couponId, long couponAmount);
}
