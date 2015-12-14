package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;

import java.util.Map;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);

    String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString);

    String investFeeCallback(Map<String, String> paramsMap, String originalQueryString);

    boolean postRepayCallback(long loanRepayId);
}
