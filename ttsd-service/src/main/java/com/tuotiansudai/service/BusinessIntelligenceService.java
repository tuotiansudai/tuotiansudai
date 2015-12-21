package com.tuotiansudai.service;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.dto.RoleStage;
import com.tuotiansudai.dto.UserStage;
import com.tuotiansudai.repository.model.KeyValueModel;

import java.util.Date;
import java.util.List;

public interface BusinessIntelligenceService {
    List<KeyValueModel> queryUserRegisterTrend(Granularity granularity, Date startTime, Date endTime, String province,UserStage userStage,RoleStage roleStage);

    List<KeyValueModel> queryUserRechargeTrend(Granularity granularity, Date startTime, Date endTime, String province);

    List<KeyValueModel> queryUserWithdrawTrend(Granularity granularity, Date startTime, Date endTime, String province);

    List<KeyValueModel> queryInvestViscosity(Date startTime, Date endTime, String province);

    List<KeyValueModel> queryUserInvestCountTrend(Date startTime, Date endTime, String province);

    List<KeyValueModel> queryUserInvestAmountTrend(Granularity granularity, Date startTime, Date endTime, String province, RoleStage roleStage);

    List<KeyValueModel> queryUserAgeTrend(Date startTime, Date endTime, String province, String isInvestor);
}
