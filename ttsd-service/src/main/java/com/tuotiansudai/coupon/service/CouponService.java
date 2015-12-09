package com.tuotiansudai.coupon.service;

public interface CouponService {
    void afterUserRegistered(String loginName);

    void afterGetLoanDetail(String loginName, long loanId);

    void afterInvest(String loginName, long loanId);

    void afterRepay(long loanId, boolean isAdvanced);
}
