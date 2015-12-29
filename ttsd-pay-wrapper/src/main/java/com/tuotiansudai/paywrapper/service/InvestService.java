package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AutoInvestMonthPeriod;

import java.util.List;
import java.util.Map;

public interface InvestService {

    BaseDto<PayFormDataDto> invest(InvestDto dto);

    String investCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<PayDataDto> asyncInvestCallback();

    String overInvestPaybackCallback(Map<String, String> paramsMap, String queryString);

    void autoInvest(long loanId);

    List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period);

    void notifyInvestorRepaySuccessfulByEmail(long loanId,int period);

    void processOneCallback(InvestNotifyRequestModel callbackRequestModel);

}
