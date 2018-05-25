package com.tuotiansudai.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
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
    BankAsyncMessage invest(InvestDto investDto) throws InvestException;

    long estimateInvestIncome(long loanId, double investFeeRate, String loginName, long amount, Date investTime);

    BasePaginationDataDto<InvestorInvestPaginationItemDataDto> getInvestPagination(String investorLoginName,
                                                                                   int index,
                                                                                   int pageSize,
                                                                                   Date startTime,
                                                                                   Date endTime,
                                                                                   LoanStatus loanStatus);

    InvestModel findById(long investId);

    BankReturnCallbackMessage noPasswordInvest(InvestDto investDto) throws InvestException;

    boolean switchNoPasswordInvest(String loginName, boolean isTurnOn, String ip);

    void markNoPasswordRemind(String loginName);

    long calculateMembershipPreference(String loginName, long loanId, List<Long> couponIds, long investAmount, Source source);

    List<InvestModel> findContractFailInvest(long loanId);

    BasePaginationDataDto<UserInvestRecordDataDto> generateUserInvestList(String loginName, int index,
                                                                          int pageSize,
                                                                          LoanStatus loanStatus);

    InvestorInvestDetailDto getInvestDetailById(long investId);
}
