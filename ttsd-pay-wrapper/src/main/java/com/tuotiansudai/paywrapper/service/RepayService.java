package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(long loanId);

    String repayCallback(Map<String, String> paramsMap, String originalQueryString);

    String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString);

    String investFeeCallback(Map<String, String> paramsMap, String originalQueryString);

    void postRepayCallback(long loanRepayId);
}
