package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;

public interface CreditLoanBillService {

    void transferOut(long orderId, long amount, CreditLoanBillBusinessType businessType, String detail, String loginName);

    void transferIn(long orderId, long amount, CreditLoanBillBusinessType businessType, String detail, String loginName);
}
