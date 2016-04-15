package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getInvestUserCoupons(String loginName, long loanId);

    List<UserCouponView> getUnusedUserCoupons(String loginName);

    List<UserCouponView> findUseRecords(String loginName);

    List<UserCouponView> getExpiredUserCoupons(String loginName);

    UserCouponDto getMaxBenefitUserCoupon(String loginName, long loanId, long amount);
}
