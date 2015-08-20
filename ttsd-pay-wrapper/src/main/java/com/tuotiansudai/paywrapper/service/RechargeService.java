package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;

import java.util.Map;

public interface RechargeService {

    BaseDto<PayFormDataDto> recharge(RechargeDto dto);

    String rechargeCallback(Map<String, String> paramsMap, String queryString);
}
