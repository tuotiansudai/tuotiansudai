package com.tuotiansudai.paywrapper.coupon.service;

public interface CouponRepayService {

    void repay(long loanRepayId);

    void generateCouponRepay(long loanId);
}
