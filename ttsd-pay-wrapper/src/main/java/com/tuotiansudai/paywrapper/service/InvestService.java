package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;

import java.util.List;
import java.util.Map;

public interface InvestService {

    BaseDto<PayFormDataDto> invest(InvestDto dto);

    String investCallback(Map<String, String> paramsMap, String queryString);

    BaseDto<BaseDataDto> asyncInvestCallback();

    String overInvestPaybackCallback(Map<String, String> paramsMap, String queryString);

    public void autoInvest(long loanId);

    public List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period);

}
