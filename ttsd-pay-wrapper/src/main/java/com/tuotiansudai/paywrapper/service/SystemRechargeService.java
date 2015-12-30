package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;

import java.util.Map;

public interface SystemRechargeService {

    BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto dto);

    String systemRechargeCallback(Map<String, String> paramsMap, String queryString);
}
