package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.SystemBillBusinessType;

public interface CreditLoanBillService {

    void transferOut(long orderId, long amount, SystemBillBusinessType businessType, String detail);

    void transferIn(long orderId, long amount, SystemBillBusinessType businessType, String detail);
}
