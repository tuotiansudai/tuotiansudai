package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.Date;

public interface InvestService {

    BaseDto<PayFormDataDto> invest(InvestDto investDto);

    long calculateExpectedInterest(long loanId, long amount);

    BasePaginationDataDto<InvestPaginationItemDataDto> getInvestPagination(String loginName,
                                                                           long loanId,
                                                                           int index,
                                                                           int pageSize,
                                                                           Date startTime,
                                                                           Date endTime,
                                                                           InvestStatus investStatus,
                                                                           LoanStatus loanStatus);
}
