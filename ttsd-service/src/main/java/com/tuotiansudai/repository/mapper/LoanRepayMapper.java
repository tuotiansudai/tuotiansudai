package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRepayModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepayMapper {

    void create(List<LoanRepayModel> loanRepayModels);

    List<LoanRepayModel> findByLoanId(long loanId);
}
