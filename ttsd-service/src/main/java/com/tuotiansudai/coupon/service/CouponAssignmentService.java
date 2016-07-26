package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;

import java.util.List;

public interface CouponAssignmentService {

    void assignUserCoupon(String loginNameOrMobile, String exchangeCode);

    void assignUserCoupon(String loginNameOrMobile, long couponId);

    void assignUserCoupon(String loginNameOrMobile, List<UserGroup> userGroups);

    UserCouponModel assign(String loginName, long couponId, String exchangeCode,Long loanId);

    void assignUserCoupon(long loanId,String loginNameOrMobile, long couponId);
}
