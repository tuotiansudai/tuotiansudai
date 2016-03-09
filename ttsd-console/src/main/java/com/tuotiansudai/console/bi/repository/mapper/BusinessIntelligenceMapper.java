package com.tuotiansudai.console.bi.repository.mapper;

import com.tuotiansudai.console.bi.dto.Granularity;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.bi.dto.UserStage;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailView;
import com.tuotiansudai.console.bi.repository.model.KeyValueModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BusinessIntelligenceMapper {
    List<KeyValueModel> queryUserRegisterTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity,
                                               @Param("province") String province, @Param("userStage") UserStage userStage, @Param("roleStage") RoleStage roleStage, @Param("channel") String channel);

    List<KeyValueModel> queryUserRechargeTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province);

    List<KeyValueModel> queryUserWithdrawTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province);

    List<KeyValueModel> queryInvestViscosity(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province);

    List<InvestViscosityDetailView> queryInvestViscosityDetail(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("loanCount") int loanCount, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    int queryInvestViscosityDetailCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("loanCount") int loanCount);

    long queryInvestViscositySumAmount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("loanCount") int loanCount);

    List<KeyValueModel> queryUserInvestCountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province);

    List<KeyValueModel> queryUserInvestAmountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity,
                                                   @Param("province") String province, @Param("roleStage") RoleStage roleStage, @Param("channel") String channel);

    List<KeyValueModel> queryUserAgeTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("isInvestor") String isInvestor);

    List<KeyValueModel> queryLoanAmountDistribution(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryLoanRaisingTimeCostingTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryWithdrawUserCountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("granularity") Granularity granularity);
}
