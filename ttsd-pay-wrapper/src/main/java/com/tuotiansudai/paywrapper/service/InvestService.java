package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface InvestService {

    BaseDto<PayFormDataDto> invest(InvestDto dto);

    BaseDto<PayDataDto> investNopwd(InvestDto dto);

    String investCallback(Map<String, String> paramsMap, String queryString);
}
