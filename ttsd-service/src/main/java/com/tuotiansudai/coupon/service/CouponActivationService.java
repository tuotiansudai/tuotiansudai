package com.tuotiansudai.coupon.service;

public interface CouponActivationService {

    void active(String loginNameLoginName, long couponId);

    void inactive(String loginNameLoginName, long couponId);

    void sendSms(long couponId);
}
