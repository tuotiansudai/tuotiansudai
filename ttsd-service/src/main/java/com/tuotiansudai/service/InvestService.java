package com.tuotiansudai.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.Date;

public interface InvestService {
    /**
     * 进行一次投资
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException;

    long calculateExpectedInterest(long loanId, long amount);

    BasePaginationDataDto<InvestPaginationItemDataDto> getInvestPagination(Long loanId, String investorLoginName,
                                                                           int index,
                                                                           int pageSize,
                                                                           Date startTime,
                                                                           Date endTime,
                                                                           InvestStatus investStatus,
                                                                           LoanStatus loanStatus);


    void turnOnAutoInvest(AutoInvestPlanModel model);

    void turnOffAutoInvest(String loginName);

    AutoInvestPlanModel findUserAutoInvestPlan(String loginName);
}
