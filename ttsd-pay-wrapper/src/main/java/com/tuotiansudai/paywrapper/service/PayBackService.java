package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface PayBackService {

    BaseDto<PayFormDataDto> payBack(InvestDto dto);

    String payBackCallback(Map<String, String> paramsMap, String queryString);
}
