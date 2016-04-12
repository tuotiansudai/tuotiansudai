package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import java.util.Map;

public interface BindBankCardService {

    BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto);

    String bindBankCardCallback(Map<String, String> paramsMap, String queryString);

    String bindBankCardApplyCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto);

    String replaceBankCardCallback(Map<String, String> paramsMap, String originalQueryString);

}
