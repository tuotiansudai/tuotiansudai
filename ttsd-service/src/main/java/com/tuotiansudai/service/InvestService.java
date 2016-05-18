package com.tuotiansudai.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.*;

import java.util.Date;
import java.util.List;

public interface InvestService {
    /**
     * 进行一次投资
     *
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException;

    long estimateInvestIncome(long loanId, long amount);

    BasePaginationDataDto<InvestorInvestPaginationItemDataDto> getInvestPagination(String investorLoginName,
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

    AutoInvestPlanModel findAutoInvestPlan(String loginName);

    List<String> findAllChannel();

    List<String> findAllInvestChannels();

    InvestModel findById(long investId);

    InvestModel findLatestSuccessInvest(String loginName);

    BaseDto<PayDataDto> noPasswordInvest(InvestDto investDto) throws InvestException;

    boolean switchNoPasswordInvest(String loginName, boolean isTurnOn);

    void markNoPasswordRemind(String loginName);

    boolean isRemindNoPassword(String loginName);
}
