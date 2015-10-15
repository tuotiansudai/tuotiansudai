package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface AdvanceRepayService {

    BaseDto<PayFormDataDto> repay(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);
}
