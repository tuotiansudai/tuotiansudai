package com.tuotiansudai.coupon.service;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;

import java.util.List;

public interface CouponAssignmentService {

    UserCouponModel assignUserCoupon(String loginNameOrMobile, String exchangeCode);

    void assignUserCoupon(String loginNameOrMobile, long couponId);

    List<CouponModel> asyncAssignUserCoupon(String loginNameOrMobile, List<UserGroup> userGroups);

    UserCouponModel assign(String loginName, long couponId, String exchangeCode);

    boolean assignInvestAchievementUserCoupon(String loginNameOrMobile, long loanId, long couponId);
}
