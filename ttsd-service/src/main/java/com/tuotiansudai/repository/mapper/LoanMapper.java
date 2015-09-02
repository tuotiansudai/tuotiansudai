package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import org.apache.ibatis.annotations.Param;

public interface LoanMapper {
    void create(LoanModel loanModel);
    LoanModel findById(@Param(value = "loanId") long loanId);
    void update(LoanModel loanModel);
}
