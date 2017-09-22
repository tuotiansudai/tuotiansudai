package com.tuotiansudai.paywrapper.credit;

import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.CreditLoanBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditLoanBillService {

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;

    @Transactional
    public void transferOut(long orderId, long amount, CreditLoanBillBusinessType businessType, String detail, String mobile) {
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel(orderId, amount, CreditLoanBillOperationType.OUT, businessType, detail, mobile);
        creditLoanBillMapper.create(creditLoanBillModel);
    }

    public void transferIn(long orderId, long amount, CreditLoanBillBusinessType businessType, String detail, String mobile) {
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel(orderId, amount, CreditLoanBillOperationType.IN, businessType, detail, mobile);
        creditLoanBillMapper.create(creditLoanBillModel);
    }
}
