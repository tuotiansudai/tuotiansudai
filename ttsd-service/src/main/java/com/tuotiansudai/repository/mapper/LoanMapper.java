package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import org.apache.ibatis.annotations.Param;

/**
 * Created by tuotian on 15/8/17.
 */
public interface LoanMapper {
    void createLoan(LoanModel loanModel);
    LoanModel findLoanByLoanId(@Param(value = "loanId")String loanId);
}
