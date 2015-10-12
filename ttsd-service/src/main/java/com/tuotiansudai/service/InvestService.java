package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;

import java.util.List;

public interface InvestService {

    /**
     * 进行一次投资
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto);

    long calculateExpectedInterest(long loanId, long amount);
    /**
     * 查找投资记录
     * @param queryDto
     * @param includeNextRepay 是否同时查询下次还款情况
     * @return
     */
    BasePaginationDataDto<InvestDetailDto> queryInvests(InvestDetailQueryDto queryDto, boolean includeNextRepay);

    void turnOnAutoInvest(AutoInvestPlanModel model);

    void turnOffAutoInvest(String loginName);

    AutoInvestPlanModel findUserAutoInvestPlan(String loginName);

    List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period);

    void validateAutoInvest(long loanId);
}
