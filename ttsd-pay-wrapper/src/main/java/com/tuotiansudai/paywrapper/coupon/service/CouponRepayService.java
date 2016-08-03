package com.tuotiansudai.paywrapper.coupon.service;

public interface CouponRepayService {

    void repay(long loanRepayId,boolean isAdvanced);

    void generateCouponRepay(long loanId);

}
