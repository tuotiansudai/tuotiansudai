package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;

import java.util.Map;

public interface WithdrawService {

    BaseDto<PayFormDataDto> withdraw(WithdrawDto dto);

    String withdrawCallback(Map<String, String> paramsMap, String originalQueryString);
}
