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

    long estimateInvestIncome(long loanId, String loginName, long amount);

    BasePaginationDataDto<InvestorInvestPaginationItemDataDto> getInvestPagination(String investorLoginName,
                                                                                   int index,
                                                                                   int pageSize,
                                                                                   Date startTime,
                                                                                   Date endTime,
                                                                                   LoanStatus loanStatus);

    InvestPaginationDataDto getInvestPagination(Long loanId, String investorMobile, String channel, Source source,
                                                Role role, Date startTime, Date endTime, InvestStatus investStatus,
                                                PreferenceType preferenceType, int index, int pageSize);

    boolean turnOnAutoInvest(String loginName, AutoInvestPlanDto model, String ip);

    boolean turnOffAutoInvest(String loginName, String ip);

    AutoInvestPlanModel findAutoInvestPlan(String loginName);

    List<String> findAllChannel();

    List<String> findAllInvestChannels();

    InvestModel findById(long investId);

    InvestModel findLatestSuccessInvest(String loginName);

    BaseDto<PayDataDto> noPasswordInvest(InvestDto investDto) throws InvestException;

    boolean switchNoPasswordInvest(String loginName, boolean isTurnOn, String ip);

    void markNoPasswordRemind(String loginName);
}
