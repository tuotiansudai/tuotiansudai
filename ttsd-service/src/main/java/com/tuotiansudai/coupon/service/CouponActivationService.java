package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.repository.model.UserGroup;

import java.util.List;

public interface CouponActivationService {

    void active(String loginNameLoginName, long couponId, String ip);

    void inactive(String loginNameLoginName, long couponId, String ip);

    void sendSms(long couponId);

    void assignUserCoupon(String loginName, List<UserGroup> userGroups);
}
