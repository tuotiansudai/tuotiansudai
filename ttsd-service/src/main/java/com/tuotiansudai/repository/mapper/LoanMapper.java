package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.apache.ibatis.annotations.Param;

public interface LoanMapper {
    void create(LoanModel loanModel);
    LoanModel findById(@Param(value = "loanId") long loanId);
    void updateStatus(@Param(value = "loanId") long loanId, @Param(value = "status") LoanStatus status);
}
