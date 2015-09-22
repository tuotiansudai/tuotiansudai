package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.RepayStatus;

public interface LoanRepayService {

    BaseDto<BasePaginationDataDto> findLoanRepayPagination(int index,int pageSize,String loanId,
                                                     String loginName,String repayStartDate,String repayEndDate,RepayStatus repayStatus);

}
