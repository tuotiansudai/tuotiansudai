package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AdvanceRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.NormalRepayNotifyRequestModel;

import java.util.Map;

public interface AdvanceRepayService {

    BaseDto<PayFormDataDto> generateRepayFormData(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString) throws Exception;

    String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) throws Exception;

    String investFeeCallback(Map<String, String> paramsMap, String originalQueryString);

    boolean paybackInvest(long loanRepayId);

    BaseDto<PayDataDto> asyncAdvanceRepayPaybackCallback();
}
