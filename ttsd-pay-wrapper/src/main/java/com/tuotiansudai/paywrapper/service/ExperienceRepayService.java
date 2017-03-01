package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.AmountTransferException;

import java.util.Map;

public interface ExperienceRepayService {

    boolean repay(long investId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);

    BaseDto<PayDataDto> postCallback(long investRepayId) throws AmountTransferException;
}
