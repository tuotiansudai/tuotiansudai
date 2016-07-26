package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanDetailsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDetailsMapper {
    void create(LoanDetailsModel loanDetailsModel);

    LoanDetailsModel getLoanDetailsByLoanId(@Param(value = "loanId") long loanId);
}
