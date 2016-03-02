package com.tuotiansudai.point.service;


import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;

public interface PointService {

    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);

}
