package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getUserCoupons(String loginName);

    UserCouponDto getUsableNewbieCoupon(String loginName);

    List<UserCouponDto> getUsableCoupons(String loginName, final long loanId, long amount);

    BaseDto<BasePaginationDataDto> findUseRecords(String loginName, int index, int pageSize);
}
