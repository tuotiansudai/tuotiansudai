package com.tuotiansudai.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public interface InvestService {
    /**
     * 进行一次投资
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException;

    long estimateInvestIncome(long loanId, long amount);

    BasePaginationDataDto<InvestPaginationItemDataDto> getInvestPagination(String investorLoginName,
                                                                           int index,
                                                                           int pageSize,
                                                                           Date startTime,
                                                                           Date endTime,
                                                                           LoanStatus loanStatus);

    long findCountInvestPagination(Long loanId, String investorLoginName,
                                   String channel, Source source, String role,
                                   Date startTime, Date endTime,
                                   InvestStatus investStatus, LoanStatus loanStatus);

    InvestPaginationDataDto getInvestPagination(Long loanId, String investorLoginName,
                                                                           String channel,
                                                                           Source source,
                                                                           String role,
                                                                           int index,
                                                                           int pageSize,
                                                                           Date startTime,
                                                                           Date endTime,
                                                                           InvestStatus investStatus,
                                                                           LoanStatus loanStatus);


    void turnOnAutoInvest(AutoInvestPlanModel model);

    void turnOffAutoInvest(String loginName);

    AutoInvestPlanModel findUserAutoInvestPlan(String loginName);

    List<String> findAllChannel();
}
