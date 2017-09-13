package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;

import java.util.Map;

public interface CreditLoanRechargeService {

    BaseDto<PayFormDataDto> creditLoanRecharge(SystemRechargeDto dto);

    String creditLoanRechargeCallback(Map<String, String> paramsMap, String queryString);
}
