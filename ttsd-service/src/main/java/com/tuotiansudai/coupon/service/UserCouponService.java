package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.UserCouponDto;

import java.util.List;

public interface UserCouponService {
    List<UserCouponDto> getUserCouponDtoByLoginName(String loginName);
}
