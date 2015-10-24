package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.RepayStatus;

import java.util.Date;

public interface LoanRepayService {

    BaseDto<BasePaginationDataDto> findLoanRepayPagination(int index, int pageSize, Long loanId,
                                                           String loginName, Date repayStartDate, Date repayEndDate, RepayStatus repayStatus);

}
