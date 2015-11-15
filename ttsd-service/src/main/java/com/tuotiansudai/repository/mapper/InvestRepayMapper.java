package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LatestInvestView;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvestRepayMapper {

    void create(List<InvestRepayModel> investRepayModels);

    List<InvestRepayModel> findByInvestId(long investId);

    List<InvestRepayModel> findByLoginNameAndInvestId(@Param(value = "loginName") String loginName, @Param(value = "investId") long investId);

    InvestRepayModel findByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    InvestRepayModel findCompletedInvestRepayByIdAndPeriod(@Param(value = "investId") long investId,@Param(value = "period") int period);

    void update(InvestRepayModel investRepayModel);

    long findByLoginNameAndTimeAndSuccessInvestRepay(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime);

    long findByLoginNameAndTimeAndNotSuccessInvestRepay(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime);

    List<InvestRepayModel> findByLoginNameAndTimeSuccessInvestRepayList(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,
                                                                        @Param(value = "startLimit") int startLimit,@Param(value = "endLimit") int endLimit);

    List<InvestRepayModel> findByLoginNameAndTimeNotSuccessInvestRepayList(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,
                                                                           @Param(value = "startLimit") int startLimit,@Param(value = "endLimit") int endLimit);

    List<LatestInvestView> findLatestInvestByLoginName(@Param(value = "loginName") String loginName,@Param(value = "startLimit") int startLimit,@Param(value = "endLimit") int endLimit);

    long findSumRepaidInterestByLoginName(@Param(value = "loginName") String loginName);

    long findSumRepayingInterestByLoginName(@Param(value = "loginName") String loginName);

    long findSumRepayingCorpusByLoginName(@Param(value = "loginName") String loginName);

    List<InvestRepayModel> findByLoanId(long loanId);
}
