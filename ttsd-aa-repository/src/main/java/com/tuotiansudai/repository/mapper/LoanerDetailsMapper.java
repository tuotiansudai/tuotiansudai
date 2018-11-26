package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanerDetailsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanerDetailsMapper {
    void create(LoanerDetailsModel loanerDetailsModel);

    LoanerDetailsModel getByLoanId(@Param(value = "loanId") long loanId);

    void deleteByLoanId(@Param(value = "loanId") long loanId);

    long getSumLoanerCountByLoanStatus(@Param(value = "loanStatusList") List<LoanStatus> loanStatusList);
}
