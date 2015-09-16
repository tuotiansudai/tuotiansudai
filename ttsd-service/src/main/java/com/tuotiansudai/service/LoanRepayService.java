package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDto;
import com.tuotiansudai.dto.LoanRepayDto;
import com.tuotiansudai.repository.model.RepayStatus;

import java.util.List;


public interface LoanRepayService {

    public BasePaginationDto findLoanRepayPagination(int index,int pageSize,String loanId,
                                                     String loginName,String repayStartDate,String repayEndDate,RepayStatus repayStatus);

}
