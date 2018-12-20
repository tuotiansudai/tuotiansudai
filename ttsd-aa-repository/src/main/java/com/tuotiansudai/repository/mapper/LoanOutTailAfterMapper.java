package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanOutTailAfterModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanOutTailAfterMapper {

    void create(LoanOutTailAfterModel model);

    void update(LoanOutTailAfterModel model);

    LoanOutTailAfterModel findByLoanId(@Param(value = "loanId") long loanId);

}
