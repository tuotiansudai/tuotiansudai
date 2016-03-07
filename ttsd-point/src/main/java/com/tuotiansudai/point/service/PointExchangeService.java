package com.tuotiansudai.point.service;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;

import java.util.List;

public interface PointExchangeService {

    List<ExchangeCouponDto> findExchangeableCouponList();

    void exchangeCoupon(long couponId, String loginName, long exchangePoint);

    boolean exchangeableCoupon(long couponId, String loginName);

}
