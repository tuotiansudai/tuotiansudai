package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestRepayInAccountDto;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvestRepayMapper {

    void create(List<InvestRepayModel> investRepayModels);

    List<InvestRepayModel> findByInvestId(long investId);

    InvestRepayModel findByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    void update(InvestRepayModel investRepayModel);

    long findByLoginNameAndTimeAndSuccessInvestRepay(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime);

    long findByLoginNameAndTimeAndNotSuccessInvestRepay(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime);

    List<InvestRepayModel> findByLoginNameAndTimeSuccessInvestRepayList(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,
                                                                        @Param(value = "startLimit") int startLimit,@Param(value = "endLimit") int endLimit);

    List<InvestRepayModel> findByLoginNameAndTimeNotSuccessInvestRepayList(@Param(value = "loginName") String loginName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,
                                                                           @Param(value = "startLimit") int startLimit,@Param(value = "endLimit") int endLimit);

    List<InvestRepayInAccountDto> findRecentlyInvestByLoginNameInAccount(@Param(value = "loginName") String loginName);

    long findSumSuccessInterestByLoginName(@Param(value = "loginName") String loginName);

    long findSumWillSuccessInterestByLoginName(@Param(value = "loginName") String loginName);

    long findSumWillSuccessCorpusByLoginName(@Param(value = "loginName") String loginName);

}
