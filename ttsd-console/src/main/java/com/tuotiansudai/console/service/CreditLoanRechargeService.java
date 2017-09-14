package com.tuotiansudai.console.service;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreditLoanRechargeService {
    @Autowired
    private PayWrapperClient payWrapperClient;

    public BaseDto<PayFormDataDto> creditLoanRecharge(InvestDto investDto) {
        return payWrapperClient.creditLoanPurchase(investDto);
    }

    public BaseDto<PayDataDto> NoPasswordCreditLoanRecharge(InvestDto investDto) {
        return payWrapperClient.noPasswordCreditLoanPurchase(investDto);
    }
}
