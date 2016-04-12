package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface AgreementService {

    BaseDto<PayFormDataDto> agreement(AgreementDto dto);

    String agreementCallback(Map<String, String> paramsMap, String queryString,AgreementBusinessType agreementBusinessType);
}
