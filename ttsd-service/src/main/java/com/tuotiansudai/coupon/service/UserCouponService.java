package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getUserAllCoupons(String loginName);

    List<UserCouponDto> getUserMoneyCoupons(String loginName);

    List<UserCouponDto> getUserInterestCoupons(String loginName);

    UserCouponDto getUsableNewbieCoupon(String loginName);

    List<UserCouponDto> getUsableCoupons(String loginName, final long loanId);

    BaseDto<BasePaginationDataDto> findMoneyCouponUseRecords(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findInterestCouponUseRecords(String loginName, int index, int pageSize);
}
