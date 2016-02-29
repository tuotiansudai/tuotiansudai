package com.tuotiansudai.point.service;


import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.point.dto.SignInPointDto;

public interface PointService {

    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);

    SignInPointDto signIn(String loginName);

    boolean signInIsSuccess(String loginName);

}
