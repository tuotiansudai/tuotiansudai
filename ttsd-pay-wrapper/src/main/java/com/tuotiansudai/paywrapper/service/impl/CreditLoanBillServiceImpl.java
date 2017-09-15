package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.service.CreditLoanBillService;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditLoanBillServiceImpl implements CreditLoanBillService {

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;

    @Override
    @Transactional
    public void transferOut(long orderId, long amount, CreditLoanBillBusinessType businessType, String detail, String loginName) {
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel(orderId, amount, SystemBillOperationType.OUT, businessType, detail, loginName);
        creditLoanBillMapper.create(creditLoanBillModel);
    }

    @Override
    @Transactional
    public void transferIn(long orderId, long amount, CreditLoanBillBusinessType businessType, String detail, String loginName) {
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel(orderId, amount, SystemBillOperationType.IN, businessType, detail, loginName);
        creditLoanBillMapper.create(creditLoanBillModel);
    }
}
