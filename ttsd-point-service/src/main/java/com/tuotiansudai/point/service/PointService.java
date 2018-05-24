package com.tuotiansudai.point.service;

import java.util.Map;

public interface PointService {
    void obtainPointInvest(long investId);

    long getAvailablePoint(String loginName);

    Map<String, String> findAllChannel();
}
