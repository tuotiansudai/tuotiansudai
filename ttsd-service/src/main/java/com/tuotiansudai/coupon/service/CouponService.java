package com.tuotiansudai.coupon.service;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.Date;
import java.util.List;

public interface CouponService {

    long estimateCouponExpectedInterest(String loginName, double investFeeRate, long loanId, List<Long> couponIds, long amount, Date investTime);

    long findExperienceInvestAmount(List<InvestModel> investModelList);

    CouponModel findExchangeableCouponById(long couponId);

    List<CouponModel> findCouponByUserGroup(List<UserGroup> userGroups);

}
