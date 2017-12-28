package com.tuotiansudai.point.service;

import com.tuotiansudai.repository.model.InvestModel;

import java.util.Map;

public interface PointService {
    void obtainPointInvest(InvestModel investModel);

    long getAvailablePoint(String loginName);

    Map<String, String> findAllChannel();
}
