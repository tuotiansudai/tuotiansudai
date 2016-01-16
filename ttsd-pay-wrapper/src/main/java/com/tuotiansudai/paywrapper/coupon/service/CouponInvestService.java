package com.tuotiansudai.paywrapper.coupon.service;


public interface CouponInvestService {

    void invest(long investId, Long userCouponId);

    void investCallback(long id);
}
