package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/8.
 */
@Repository
public interface LoanRepayMapper {

    public void insertLoanRepay(List<LoanRepayModel> loanRepayModels);

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

}
