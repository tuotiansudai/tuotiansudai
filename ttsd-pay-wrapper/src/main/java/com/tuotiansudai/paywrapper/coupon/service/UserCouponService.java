package com.tuotiansudai.paywrapper.coupon.service;


import com.tuotiansudai.dto.InvestDto;

public interface UserCouponService {

    void afterReturningInvest(InvestDto investDto, long investId);

    void recordUsedCount(long id);
}
