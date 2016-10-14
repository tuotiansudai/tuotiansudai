package com.tuotiansudai.point.service;

import com.tuotiansudai.repository.model.InvestModel;
import coupon.dto.ExchangeCouponDto;

public interface PointService {
    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);

    void obtainPointInvest(InvestModel investModel);

    long getAvailablePoint(String loginName);


}
