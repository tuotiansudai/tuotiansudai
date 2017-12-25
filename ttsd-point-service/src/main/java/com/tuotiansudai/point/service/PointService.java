package com.tuotiansudai.point.service;

import com.tuotiansudai.repository.model.InvestModel;

public interface PointService {
    void obtainPointInvest(InvestModel investModel);

    long getAvailablePoint(String loginName);
}
