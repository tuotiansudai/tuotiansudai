package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanerEnterpriseDetailsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanerEnterpriseDetailsMapper {

    void create(LoanerEnterpriseDetailsModel model);

    LoanerEnterpriseDetailsModel getByLoanId(@Param(value = "loanId") long loanId);

    void deleteByLoanId(@Param(value = "loanId") long loanId);
}
