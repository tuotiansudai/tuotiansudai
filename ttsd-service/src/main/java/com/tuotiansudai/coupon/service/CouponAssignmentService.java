package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CouponAssignmentService {

    void assignUserCoupon(String loginNameOrMobile, String exchangeCode);

    void assignUserCoupon(String loginNameOrMobile, long couponId);

    void assignUserCoupon(String loginNameOrMobile, List<UserGroup> userGroups);

    UserCouponModel assign(long couponId, String loginName, String exchangeCode);
}
