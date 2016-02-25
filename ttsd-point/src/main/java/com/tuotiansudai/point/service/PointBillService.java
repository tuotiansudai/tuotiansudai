package com.tuotiansudai.point.service;

import com.tuotiansudai.point.repository.model.PointBusinessType;

public interface PointBillService {

    void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point);
}
