package com.tuotiansudai.point.service;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.repository.model.CouponType;

import java.util.List;

public interface PointExchangeService {

    List<ExchangeCouponDto> findExchangeableCouponList();

    void exchangeCoupon(long couponId, String loginName, long exchange_point, int deadLine);

}
