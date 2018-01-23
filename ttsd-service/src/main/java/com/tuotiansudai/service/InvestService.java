package com.tuotiansudai.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public interface InvestService {
    /**
     * 进行一次投资
     *
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException;

    long estimateInvestIncome(long loanId, String loginName, long amount, Date investTime);

    BasePaginationDataDto<InvestorInvestPaginationItemDataDto> getInvestPagination(String investorLoginName,
                                                                                   int index,
                                                                                   int pageSize,
                                                                                   Date startTime,
                                                                                   Date endTime,
                                                                                   LoanStatus loanStatus);

    boolean turnOnAutoInvest(String loginName, AutoInvestPlanDto model, String ip);

    boolean turnOffAutoInvest(String loginName, String ip);

    AutoInvestPlanModel findAutoInvestPlan(String loginName);

    InvestModel findById(long investId);


    InvestModel findLatestSuccessInvest(String loginName);

    BaseDto<PayDataDto> noPasswordInvest(InvestDto investDto) throws InvestException;

    boolean switchNoPasswordInvest(String loginName, boolean isTurnOn, String ip);

    void markNoPasswordRemind(String loginName);

    long calculateMembershipPreference(String loginName, long loanId, List<Long> couponIds, long investAmount, Source source);

    List<InvestModel> findContractFailInvest(long loanId);

    boolean isNewUserForWechatLottery(String loginName);

    boolean isFirstInvest(String loginName, Date investTime);

    BasePaginationDataDto<UserInvestRecordDataDto> generateUserInvestList(String loginName, int index,
                                                                          int pageSize,
                                                                          LoanStatus loanStatus);

    InvestorInvestDetailDto getInvestDetailById(long investId);
}
