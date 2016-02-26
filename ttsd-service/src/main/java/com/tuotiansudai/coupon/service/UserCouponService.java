package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.CouponType;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getUserCoupons(String loginName, List<CouponType> couponTypeList);

    List<UserCouponDto> getUsableCoupons(String loginName, final long loanId);

    BaseDto<BasePaginationDataDto> findUseRecords(List<CouponType> couponTypeList, String loginName, int index, int pageSize);
}
