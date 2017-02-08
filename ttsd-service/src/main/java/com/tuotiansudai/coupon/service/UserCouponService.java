package com.tuotiansudai.coupon.service;

import com.tuotiansudai.dto.UserCouponDto;
import com.tuotiansudai.repository.model.UserCouponView;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getInvestUserCoupons(String loginName, long loanId);

    List<UserCouponView> getUnusedUserCoupons(String loginName);

    List<UserCouponView> findUseRecords(String loginName);

    List<UserCouponView> getExpiredUserCoupons(String loginName);

    UserCouponDto getExperienceInvestUserCoupon(String loginName);

    UserCouponDto getMaxBenefitUserCoupon(String loginName, long loanId, long amount);

    boolean isUsableUserCouponExist(String loginName);

    void updateCouponAndAssign(long investId);
}
