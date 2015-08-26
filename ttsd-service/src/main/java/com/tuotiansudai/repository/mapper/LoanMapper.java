package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import org.apache.ibatis.annotations.Param;

public interface LoanMapper {
    void create(LoanModel loanModel);
    LoanModel findLoanByLoanId(@Param(value = "loanId")long loanId);
}
