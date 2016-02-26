package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.CouponType;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getUsableCoupons(String loginName, final long loanId);

    List<UserCouponDto> getUnusedUserCoupons(String loginName);

    List<CouponUseRecordView> findUseRecords(String loginName);

    List<UserCouponDto> getExpiredUserCoupons(String loginName);
}
