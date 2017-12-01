package com.tuotiansudai.paywrapper.extrarate.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;

import java.util.Map;

public interface ExtraRateService {

    void normalRepay(long loanRepayId);

    void advanceRepay(long loanRepayId);

    String extraRateInvestCallback(Map<String, String> paramsMap, String originalQueryString);

    BaseDto<PayDataDto> asyncExtraRateInvestCallback(long notifyRequestId);
}
