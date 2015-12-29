package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.dto.UserInvestingCouponDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getUserCouponDtoByLoginName(String loginName);

    List<UserInvestingCouponDto> getValidCoupons(String loginName, final long loanId);

    BaseDto<BasePaginationDataDto> findUseRecords(String loginName, int index, int pageSize);

}
