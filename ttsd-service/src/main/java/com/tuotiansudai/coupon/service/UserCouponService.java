package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getUsableCoupons(String loginName, final long loanId);

    List<UserCouponDto> getUnusedUserCoupons(String loginName);

    List<CouponUseRecordView> findUseRecords(String loginName);

    List<UserCouponDto> getExpiredUserCoupons(String loginName);
}
