package com.tuotiansudai.point.service;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;

public interface PointService {
    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);

    long getAvailablePoint(String loginName);


}
