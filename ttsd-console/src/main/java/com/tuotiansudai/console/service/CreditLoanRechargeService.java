package com.tuotiansudai.console.service;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreditLoanRechargeService {
    @Autowired
    private PayWrapperClient payWrapperClient;

    public BaseDto<PayFormDataDto> creditLoanRecharge(CreditLoanRechargeDto creditLoanRechargeDto) {
        return payWrapperClient.creditLoanRecharge(creditLoanRechargeDto);
    }

    public BaseDto<PayDataDto> noPasswordCreditLoanRecharge(CreditLoanRechargeDto creditLoanRechargeDto) {
        return payWrapperClient.noPasswordCreditLoanRecharge(creditLoanRechargeDto);
    }
}
