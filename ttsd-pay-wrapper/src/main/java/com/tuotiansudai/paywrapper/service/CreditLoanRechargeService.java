package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface CreditLoanRechargeService {

    BaseDto<PayDataDto> creditLoanRechargeNoPwd(InvestDto dto);

    BaseDto<PayFormDataDto> creditLoanRecharge(InvestDto dto);

    String creditLoanRechargeCallback(Map<String, String> paramsMap, String queryString);
}
