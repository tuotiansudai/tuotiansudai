package com.tuotiansudai.paywrapper.extrarate.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExtraRateNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;

import java.util.Map;

public interface ExtraRateService {

    void normalRepay(long loanRepayId);

    void advanceRepay(long loanRepayId);

    String extraRateInvestCallback(Map<String, String> paramsMap, String originalQueryString);

    BaseDto<PayDataDto> asyncExtraRateInvestCallback(long notifyRequestId);

    void processOneCallback(ExtraRateNotifyRequestModel callbackRequestModel) throws Exception;

}
