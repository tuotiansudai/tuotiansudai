package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.dto.RoleStage;
import com.tuotiansudai.dto.UserStage;
import com.tuotiansudai.repository.model.InvestViscosityDetailView;
import com.tuotiansudai.repository.model.KeyValueModel;
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

    List<KeyValueModel> queryWithdrawUserCountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("granularity") Granularity granularity);
}
