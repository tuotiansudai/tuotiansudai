package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
public interface AgreementService {

    BaseDto<PayFormDataDto> agreement(AgreementDto dto);

    String agreementCallback(Map<String, String> paramsMap, String queryString);
}
