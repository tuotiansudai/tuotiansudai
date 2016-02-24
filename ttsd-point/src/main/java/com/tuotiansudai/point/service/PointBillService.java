package com.tuotiansudai.point.service;

import com.tuotiansudai.point.repository.model.PointBusinessType;

public interface PointBillService {

    void createPointBill(String loginName, PointBusinessType businessType, long point);
}
