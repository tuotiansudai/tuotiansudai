package com.tuotiansudai.console.bi.service;

import com.tuotiansudai.console.bi.dto.Granularity;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.bi.dto.UserStage;
import com.tuotiansudai.console.bi.model.InvestViscosityDetailTableView;
import com.tuotiansudai.console.bi.model.KeyValueModel;

import java.util.Date;
import java.util.List;

public interface BusinessIntelligenceService {

    List<String> getChannels();

    List<KeyValueModel> queryUserRegisterTrend(Granularity granularity, Date startTime, Date endTime, String province,UserStage userStage,RoleStage roleStage, String channel);

    List<KeyValueModel> queryUserRechargeTrend(Granularity granularity, Date startTime, Date endTime, String province);

    List<KeyValueModel> queryUserWithdrawTrend(Granularity granularity, Date startTime, Date endTime, String province);

    List<KeyValueModel> queryInvestViscosity(Date startTime, Date endTime, String province);

    InvestViscosityDetailTableView queryInvestViscosityDetail(Date startTime, Date endTime, final String province, int loanCount, int pageNo, int pageSize);

    List<KeyValueModel> queryUserInvestCountTrend(Date startTime, Date endTime, String province);

    List<KeyValueModel> queryUserInvestAmountTrend(Granularity granularity, Date startTime, Date endTime, String province, RoleStage roleStage, String channel);

    List<KeyValueModel> queryUserAgeTrend(Date startTime, Date endTime, String province, String isInvestor);

    List<KeyValueModel> queryLoanAmountDistribution(Date startTime, Date endTime);

    List<KeyValueModel> queryLoanRaisingTimeCostingTrend(Date startTime, Date endTime);

    List<KeyValueModel> queryWithdrawUserCountTrend(Date startTime, Date endTime,Granularity granularity);
}
