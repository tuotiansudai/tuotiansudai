package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.LoanRepayNotifyModel;
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
                                                 @Param(value = "startTime") Date startTime,
                                                 @Param(value = "endTime") Date endTime);

    int findLoanRepayCount(@Param(value = "loanId") Long loanId,
                           @Param(value = "loginName") String loginName,
                           @Param(value = "repayStatus") RepayStatus repayStatus,
                           @Param(value = "startTime") Date startTime,
                           @Param(value = "endTime") Date endTime);

    LoanRepayModel findById(long id);

    List<LoanRepayModel> findByLoanIdOrderByPeriodAsc(long loanId);

    List<LoanRepayModel> findByAgentAndLoanId(@Param(value = "agentLoginName") String agentLoginName,
                                              @Param(value = "loanId") long loanId);

    LoanRepayModel findEnabledLoanRepayByLoanId(long loanId);

    LoanRepayModel findByLoanIdAndPeriod(@Param(value = "loanId") long loanId,
                                         @Param(value = "period") int period);

    void update(LoanRepayModel loanRepayModel);

    long sumSuccessLoanRepayMaxPeriod(@Param(value = "loanId") long loanId);

    LoanRepayModel findWaitPayLoanRepayByLoanId(long loanId);

    LoanRepayModel findCurrentLoanRepayByLoanId(long loanId);

    long findByLoginNameAndTimeSuccessRepay(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime);

    List<LoanRepayModel> findByLoginNameAndTimeRepayList(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,
                                                         @Param(value = "startLimit") int startLimit,@Param(value = "endLimit") int endLimit);

    List<LoanRepayModel> findNotCompleteLoanRepay();

    List<LoanRepayNotifyModel> findLoanRepayNotifyToday(@Param(value = "today") String today);

    Date findLastRepayDateByLoanId(@Param(value="loanId") long loanId);
}
