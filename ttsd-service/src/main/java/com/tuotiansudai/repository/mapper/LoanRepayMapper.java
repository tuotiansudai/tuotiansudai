package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepayMapper {

    void create(List<LoanRepayModel> loanRepayModels);

    List<LoanRepayModel> findLoanRepayPagination(@Param(value = "index") int index,
                                                 @Param(value = "pageSize") int pageSize,
                                                 @Param(value = "loanId") Long loanId,
                                                 @Param(value = "loginName") String loginName,
                                                 @Param(value = "repayStatus") RepayStatus repayStatus,
                                                 @Param(value = "repayStartDate") String repayStartDate,
                                                 @Param(value = "repayEndDate") String repayEndDate);

    int findLoanRepayCount(@Param(value = "loanId") Long loanId,
                                                 @Param(value = "loginName") String loginName,
                                                 @Param(value = "repayStatus") RepayStatus repayStatus,
                                                 @Param(value = "repayStartDate") String repayStartDate,
                                                 @Param(value = "repayEndDate") String repayEndDate);

    LoanRepayModel findById(long id);

    List<LoanRepayModel> findByLoanId(long loanId);

    List<LoanRepayModel> findByLoanerAndLoanId(@Param(value = "loanerLoginName") String loanerLoginName,
                                               @Param(value = "loanId") long loanId);

    LoanRepayModel findEnabledRepayByLoanId(long loanId);

    LoanRepayModel findByLoanIdAndPeriod(@Param(value = "loanId") long loanId,
                                         @Param(value = "period") int period);

    void update(LoanRepayModel loanRepayModel);
}
