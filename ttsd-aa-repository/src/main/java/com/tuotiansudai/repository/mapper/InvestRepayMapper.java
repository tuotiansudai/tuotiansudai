package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankLoanRepayInvestDataView;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LatestInvestView;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvestRepayMapper {

    void create(List<InvestRepayModel> investRepayModels);

    InvestRepayModel findById(long id);

    List<InvestRepayModel> findByInvestIdAndPeriodAsc(long investId);

    List<InvestRepayModel> findByLoginNameAndInvestId(@Param(value = "loginName") String loginName, @Param(value = "investId") long investId);

    InvestRepayModel findByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    List<InvestRepayModel> findByInvestIdAndLTPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    InvestRepayModel findCompletedInvestRepayByIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    void update(InvestRepayModel investRepayModel);

    long findByLoginNameAndTimeAndSuccessInvestRepay(@Param(value = "loginName") String loginName, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);

    long findByLoginNameAndTimeAndNotSuccessInvestRepay(@Param(value = "loginName") String loginName, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);

    List<InvestRepayModel> findByLoginNameAndTimeSuccessInvestRepayList(@Param(value = "loginName") String loginName, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime,
                                                                        @Param(value = "startLimit") int startLimit, @Param(value = "endLimit") int endLimit);

    List<InvestRepayModel> findByLoginNameAndTimeNotSuccessInvestRepayList(@Param(value = "loginName") String loginName, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime,
                                                                           @Param(value = "startLimit") int startLimit, @Param(value = "endLimit") int endLimit);

    List<LatestInvestView> findLatestInvestByLoginName(@Param(value = "loginName") String loginName, @Param(value = "startLimit") int startLimit, @Param(value = "endLimit") int endLimit);

    long findSumRepayingInterestByLoginName(@Param(value = "loginName") String loginName);

    long findSumRepayingCorpusByLoginName(@Param(value = "loginName") String loginName);

    List<InvestRepayModel> findByLoginNameAndStatus(@Param(value = "loginName") String loginName,
                                                    @Param(value = "paidStatus") String paidStatus,
                                                    @Param(value = "rowIndex") int rowIndex,
                                                    @Param(value = "rowLimit") int rowLimit);

    long findCountByLoginNameAndStatus(@Param(value = "loginName") String loginName,
                                       @Param(value = "paidStatus") String paidStatus);

    List<InvestRepayModel> findInvestRepayByLoanIdAndPeriod(@Param(value = "loanId") long loanId, @Param(value = "period") int period);

    List<InvestRepayModel> findByLoanId(long loanId);

    int findLeftPeriodByTransferInvestIdAndPeriod(@Param(value = "transferInvestId") long transferInvestId, @Param(value = "period") int period);

    List<InvestRepayModel> findInvestRepayByLoginNameAndRepayTime(@Param(value = "loginName") String loginName,
                                                                  @Param(value = "year") String year,
                                                                  @Param(value = "month") String month,
                                                                  @Param(value = "day") String day);

    List<InvestRepayModel> findByInvestId(long investId);

    List<BankLoanRepayInvestDataView> queryBankInvestRepayData(@Param(value = "loanId") long loanId,
                                                               @Param(value = "period") int period);
}
