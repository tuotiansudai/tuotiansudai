package com.tuotiansudai.console.bi.service;

import com.tuotiansudai.console.bi.dto.Granularity;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.bi.dto.UserStage;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailTableView;
import com.tuotiansudai.console.bi.repository.model.KeyValueModel;
import com.tuotiansudai.enums.Role;

import java.util.Date;
import java.util.List;

public interface BusinessIntelligenceService {

    List<String> getChannels();

    List<KeyValueModel> queryUserRegisterTrend(Granularity granularity, Date startTime, Date endTime, String province,UserStage userStage,RoleStage roleStage, String channel);

    List<KeyValueModel> queryUserRechargeTrend(Granularity granularity, Date startTime, Date endTime, String province, String role);

    List<KeyValueModel> queryUserWithdrawTrend(Granularity granularity, Date startTime, Date endTime, String province, String role);

    List<KeyValueModel> queryInvestViscosity(Date startTime, Date endTime, String province);

    List<KeyValueModel> queryInvestCountViscosity(Date startTime, Date endTime, String province);

    InvestViscosityDetailTableView queryInvestViscosityDetail(Date startTime, Date endTime, final String province, int loanCount, int pageNo, int pageSize);

    InvestViscosityDetailTableView queryInvestCountViscosityDetail(Date startTime, Date endTime, final String province, int loanCount, int pageNo, int pageSize);

    List<KeyValueModel> queryUserInvestCountTrend(Date startTime, Date endTime, String province, Boolean isTransfer);

    List<KeyValueModel> queryUserInvestAmountTrend(Granularity granularity, Date startTime, Date endTime, String province, RoleStage roleStage, String channel, Boolean isTransfer);

    List<KeyValueModel> queryUserAgeTrend(Date startTime, Date endTime, String province, String isInvestor);

    List<KeyValueModel> queryLoanAmountDistribution(Date startTime, Date endTime);

    List<KeyValueModel> queryLoanRaisingTimeCostingTrend(Date startTime, Date endTime);

    List<KeyValueModel> queryWithdrawUserCountTrend(Date startTime, Date endTime,Granularity granularity, String role);

    List<KeyValueModel> queryPlatformSumRepay(Date startTime, Date endTime,Granularity granularity);

    List<KeyValueModel> queryPlatformOut(Date startTime, Date endTime,Granularity granularity);

    List<KeyValueModel> queryAnxinUserStatusStatistics(Date startTime, Date endTime);

    List<KeyValueModel> queryLoanOutAmountTrend(Date startTime, Date endTime,Granularity granularity);

}
