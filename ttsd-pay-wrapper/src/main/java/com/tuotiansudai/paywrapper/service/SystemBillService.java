package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.SystemBillBusinessType;
import org.springframework.transaction.annotation.Transactional;

public interface SystemBillService {

    void transferOut(long orderId, long amount, SystemBillBusinessType businessType, String detail);

    void transferIn(long orderId, long amount, SystemBillBusinessType businessType, String detail);
}
