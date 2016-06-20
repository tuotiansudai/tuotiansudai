package com.tuotiansudai.coupon.service;

public interface CouponActivationService {

    void active(String loginNameLoginName, long couponId, String ip);

    void inactive(String loginNameLoginName, long couponId, String ip);

    void sendSms(long couponId);
}
