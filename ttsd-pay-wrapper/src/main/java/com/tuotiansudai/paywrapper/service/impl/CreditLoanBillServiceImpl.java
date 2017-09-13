package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditLoanBillServiceImpl implements SystemBillService {

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;

    @Override
    @Transactional
    public void transferOut(long orderId, long amount, SystemBillBusinessType businessType, String detail) {
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel(orderId, amount, SystemBillOperationType.OUT, businessType, detail);
        creditLoanBillMapper.create(creditLoanBillModel);
    }

    @Override
    @Transactional
    public void transferIn(long orderId, long amount, SystemBillBusinessType businessType, String detail) {
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel(orderId, amount, SystemBillOperationType.IN, businessType, detail);
        creditLoanBillMapper.create(creditLoanBillModel);
    }
}
