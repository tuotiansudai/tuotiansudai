package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.List;
import java.util.Map;

public interface NormalRepayService {

    BaseDto<PayFormDataDto> repay(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);
}
