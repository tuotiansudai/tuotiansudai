package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.loanout.impl.LoanServiceImpl;
import com.tuotiansudai.paywrapper.service.CreditLoanOutService;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class CreditLoanOutServiceImpl implements CreditLoanOutService {

    private final static Logger logger = Logger.getLogger(CreditLoanOutServiceImpl.class);

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;
    @Autowired
    private CreditLoanRechargeMapper creditLoanRechargeMapper;


    @Override
    public void creditLoanOut(){
        long sumAmountByIncome = creditLoanBillMapper.findSumAmountByIncome(CreditLoanBillBusinessType.XYD_USER_REPAY, SystemBillOperationType.IN);
        if (sumAmountByIncome <= 0){
            return;
        }

        logger.info("[标的放款]：发起联动优势放款请求，标的ID:" + null + "，代理人:" + null + "，放款金额:" + null);


    }

}
