package com.tuotiansudai.paywrapper.coupon.service;

public interface CouponRepayService {

    void repay(long loanRepayId);

    void generateCouponPayment(long loanId);
}
