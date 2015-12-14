package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.exception.CreateCouponException;

import java.util.List;

public interface CouponService {

    void createCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    void afterReturningUserRegistered(String loginName);

    void afterReturningInvest(InvestDto investDto);

    void afterReturningRepay(long loanId, boolean isAdvanced);

    List<CouponModel> findCoupons(int index, int pageSize);

    int findCouponsCount();

    void updateCoupon(String loginName, long couponId, boolean active);

    CouponModel findCouponById (long couponId);

}
