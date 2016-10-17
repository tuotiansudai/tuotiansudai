package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;

import java.util.Date;
import java.util.List;

public interface LoanService {

    LoanModel findLoanById(long loanId);

    List<LoanItemDto> findLoanItems(String name, LoanStatus status, double rateStart, double rateEnd, int durationStart, int durationEnd, int currentPageNo);

    int findLoanListCountWeb(String name, LoanStatus status, double rateStart, double rateEnd,int durationStart,int durationEnd);

    BaseDto<BasePaginationDataDto> getLoanerLoanData(String loginName, int index, int pageSize, LoanStatus status, Date startTime, Date endTime);

    int findLoanListCount(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime);

    List<LoanListDto> findLoanList(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize);
}
