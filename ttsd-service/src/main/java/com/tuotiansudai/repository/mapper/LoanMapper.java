package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanMapper {
    void create(LoanModel loanModel);
    LoanModel findById(@Param(value = "loanId") long loanId);

    void update(LoanModel loanModel);

    List<LoanModel> findByStatus(@Param(value = "status")LoanStatus status);

    void updateStatus(@Param(value = "loanId") long loanId, @Param(value = "status") LoanStatus status);
}
