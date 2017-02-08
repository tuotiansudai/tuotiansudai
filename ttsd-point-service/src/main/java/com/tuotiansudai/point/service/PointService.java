package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.ExchangeCouponDto;
import com.tuotiansudai.repository.model.InvestModel;

public interface PointService {
    void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto);

    void obtainPointInvest(InvestModel investModel);

    long getAvailablePoint(String loginName);


}
