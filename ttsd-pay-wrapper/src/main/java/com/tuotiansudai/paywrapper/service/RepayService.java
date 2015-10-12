package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.List;
import java.util.Map;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(long loanId);

    BaseDto<PayFormDataDto> advanceRepay(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);

    void generateRepay(long loanId);
}
