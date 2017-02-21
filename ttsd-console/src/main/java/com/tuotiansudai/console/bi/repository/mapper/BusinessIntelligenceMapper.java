package com.tuotiansudai.console.bi.repository.mapper;

import com.tuotiansudai.console.bi.dto.Granularity;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.bi.dto.UserStage;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailView;
import com.tuotiansudai.console.bi.repository.model.KeyValueModel;
import com.tuotiansudai.enums.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BusinessIntelligenceMapper {
    List<KeyValueModel> queryUserRegisterTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity,
                                               @Param("province") String province, @Param("userStage") UserStage userStage, @Param("roleStage") RoleStage roleStage, @Param("channel") String channel);

    List<KeyValueModel> queryUserRechargeTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province, @Param("role") Role role);

    List<KeyValueModel> queryUserWithdrawTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province, @Param("role") Role role);

    List<KeyValueModel> queryInvestViscosity(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province);

    List<KeyValueModel> queryInvestCountViscosity(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province);

    List<InvestViscosityDetailView> queryInvestViscosityDetail(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("loanCount") int loanCount, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<InvestViscosityDetailView> queryInvestCountViscosityDetail(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("investCount") int investCount, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    int queryInvestViscosityDetailCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("loanCount") int loanCount);

    int queryInvestCountViscosityDetailCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("investCount") int investCount);

    long queryInvestViscositySumAmount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("loanCount") int loanCount);

    long queryInvestCountViscositySumAmount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("investCount") int investCount);

    List<KeyValueModel> queryUserInvestCountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("isTransfer") Boolean isTransfer);

    List<KeyValueModel> queryUserInvestAmountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity,
                                                   @Param("province") String province, @Param("roleStage") RoleStage roleStage, @Param("channel") String channel, @Param("isTransfer") Boolean isTransfer);

    List<KeyValueModel> queryUserAgeTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province, @Param("isInvestor") String isInvestor);

    List<KeyValueModel> queryLoanAmountDistribution(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryLoanRaisingTimeCostingTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryWithdrawUserCountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("granularity") Granularity granularity, @Param("role") Role role);

    KeyValueModel queryRepayByRecheckTimeAndActualRepayDate(@Param("repayDate") Date repayDate);

    List<KeyValueModel> querySystemBillOutByCreatedTime(@Param(value = "startTime") Date startTime,
                                                        @Param(value = "endTime") Date endTime,
                                                        @Param("granularity") Granularity granularity);

    List<KeyValueModel> queryAnxinUserStatus(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryAnxinInvestSuccess(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
