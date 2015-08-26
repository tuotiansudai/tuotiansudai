package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanMapper {
    void createLoan(LoanModel loanModel);
    LoanModel findLoanByLoanId(@Param(value = "loanId")long loanId);

    public List<LoanListDto> findLoanList();
}
