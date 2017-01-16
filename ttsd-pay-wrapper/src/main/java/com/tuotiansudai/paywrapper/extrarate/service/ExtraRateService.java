package com.tuotiansudai.paywrapper.extrarate.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExtraRateNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;

import java.util.Map;

public interface ExtraRateService {

    boolean normalRepay(long loanRepayId);

    boolean advanceRepay(long loanRepayId);

    void transferPurchase(long investId);

    String extraRateInvestCallback(Map<String, String> paramsMap, String originalQueryString);

    BaseDto<PayDataDto> asyncExtraRateInvestCallback();

    void processOneCallback(ExtraRateNotifyRequestModel callbackRequestModel) throws Exception;

}
