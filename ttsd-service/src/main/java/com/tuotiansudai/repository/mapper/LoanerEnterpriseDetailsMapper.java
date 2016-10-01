package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanerDetailsModel;
import com.tuotiansudai.repository.model.LoanerEnterpriseDetailsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanerEnterpriseDetailsMapper {

    void create(LoanerEnterpriseDetailsModel model);

    void update(LoanerDetailsModel loanerDetailsModel);

    LoanerEnterpriseDetailsModel getByLoanId(@Param(value = "loanId") long loanId);
}
