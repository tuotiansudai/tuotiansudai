package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepayMapper {

    void create(List<LoanRepayModel> loanRepayModels);

    LoanRepayModel findById(long id);

    List<LoanRepayModel> findByLoanId(long loanId);

    List<LoanRepayModel> findByLoanerAndLoanId(@Param(value = "loanerLoginName") String loanerLoginName,
                                               @Param(value = "loanId") long loanId);

    LoanRepayModel findEnabledRepayByLoanId(long loanId);

    LoanRepayModel findByLoanIdAndPeriod(@Param(value = "loanId") long loanId,
                                         @Param(value = "period") int period);

    void update(LoanRepayModel loanRepayModel);
}
