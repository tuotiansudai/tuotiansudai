package com.tuotiansudai.console.service;

public interface CouponActivationService {
    void inactive(String loginName, long couponId, String ip);

    void exchangeInactive(String loginName, long couponId, String ip);

    void active(String loginName, long couponId, String ip);
}
