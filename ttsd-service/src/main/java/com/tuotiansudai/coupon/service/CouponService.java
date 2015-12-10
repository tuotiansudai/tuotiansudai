package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.exception.CreateCouponException;

import java.util.List;

public interface CouponService {

    void createCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    void afterUserRegistered(String loginName);

    void afterInvest(String loginName, long loanId);

    void afterRepay(long loanId, boolean isAdvanced);

    void activeCoupon(String loginName,long couponId);

    List<CouponModel> findCoupons(int index, int pageSize);

    int findCouponsCount();
}
