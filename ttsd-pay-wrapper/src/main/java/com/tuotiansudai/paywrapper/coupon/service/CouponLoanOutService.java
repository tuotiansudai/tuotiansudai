package com.tuotiansudai.paywrapper.coupon.service;

public interface CouponLoanOutService {

    boolean sendRedEnvelope(long loanId);

    boolean assignInvestAchievementUserCoupon(long loanId);

}
