package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.List;

public interface CouponService {

    long estimateCouponExpectedInterest(String loginName, long loanId, List<Long> couponIds, long amount);

    long findExperienceInvestAmount(List<InvestModel> investModelList);

    CouponModel findExchangeableCouponById(long couponId);

}
