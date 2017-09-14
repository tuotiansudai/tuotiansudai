package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuRepayDto;
import com.tuotiansudai.dto.PayFormDataDto;

import java.util.Map;

public interface HuiZuRepayService {

    BaseDto<PayFormDataDto> passwordRepay(HuiZuRepayDto huiZuRepayDto);

    String huiZuRepayCallback(Map<String, String> paramsMap, String originalQueryString);

    void hzRepayModify(long notifyRequestId);


}
