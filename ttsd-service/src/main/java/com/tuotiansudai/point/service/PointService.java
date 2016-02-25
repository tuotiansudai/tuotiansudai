package com.tuotiansudai.point.service;


import com.tuotiansudai.point.dto.ExchangeCouponDto;

public interface PointService {

    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);
}
