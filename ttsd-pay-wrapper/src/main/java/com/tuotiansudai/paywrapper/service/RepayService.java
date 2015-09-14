package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface RepayService {

    @Transactional
    BaseDto<PayFormDataDto> repay(RepayDto repayDto);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);

    void generateRepay(long loanId);

}
