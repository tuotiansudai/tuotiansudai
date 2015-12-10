package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.CreateCouponException;

public interface CouponService {

    void createCoupon(String loginName,CouponDto couponDto) throws CreateCouponException;

    void afterUserRegistered(String loginName);

    void afterInvest(String loginName, long loanId);

    void afterRepay(long loanId, boolean isAdvanced);

    void activeCoupon(String loginName,long couponId);
}
