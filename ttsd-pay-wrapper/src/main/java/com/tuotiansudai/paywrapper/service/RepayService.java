package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface RepayService {

    boolean autoRepay(long loanId);

    BaseDto<PayFormDataDto> repay(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);

    String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString);

    String investFeeCallback(Map<String, String> paramsMap, String originalQueryString);

    boolean postRepayCallback(long loanRepayId);
}
