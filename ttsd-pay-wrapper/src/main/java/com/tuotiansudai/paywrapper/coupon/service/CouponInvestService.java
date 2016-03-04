package com.tuotiansudai.paywrapper.coupon.service;


import java.util.List;

public interface CouponInvestService {

    void invest(long investId, List<Long> userCouponIds);

    void investCallback(long id);

    void cancelUserCoupon(long loanId);
}
