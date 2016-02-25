package com.tuotiansudai.point.service;


import com.tuotiansudai.point.repository.model.ExchangeCouponDto;

public interface PointService {

    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);
}
