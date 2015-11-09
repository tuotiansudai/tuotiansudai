package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;

import java.util.List;
import java.util.Map;

public interface InvestService {

    BaseDto<PayFormDataDto> invest(InvestDto dto);

    String investCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<PayDataDto> asyncInvestCallback();

    String overInvestPaybackCallback(Map<String, String> paramsMap, String queryString);

    void autoInvest(long loanId);

    List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period);

}
