package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanerDetailsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanerDetailsMapper {
    void create(LoanerDetailsModel loanerDetailsModel);

    LoanerDetailsModel getLoanerDetailByLoanId(@Param(value = "loanId") long loanId);
}
