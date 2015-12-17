package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.dto.UserStage;
import com.tuotiansudai.repository.mapper.BusinessIntelligenceMapper;
import com.tuotiansudai.repository.model.KeyValueModel;
import com.tuotiansudai.service.BusinessIntelligenceService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BusinessIntelligenceServiceImpl implements BusinessIntelligenceService {

    @Autowired
    private BusinessIntelligenceMapper businessIntelligenceMapper;

    @Override
    public List<KeyValueModel> queryUserRegisterTrend(Granularity granularity, Date startTime, Date endTime, String province,UserStage userStage) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserRegisterTrend(queryStartTime, queryEndTime, granularity, province,userStage);
    }

    @Override
    public List<KeyValueModel> queryUserRechargeTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserRechargeTrend(queryStartTime, queryEndTime, granularity, province);
    }

    @Override
    public List<KeyValueModel> queryUserWithdrawTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserWithdrawTrend(queryStartTime, queryEndTime, granularity, province);
    }

    @Override
    public List<KeyValueModel> queryUserAccountTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserAccountTrend(queryStartTime, queryEndTime, granularity, province);
    }

    @Override
    public List<KeyValueModel> queryUserInvestCountTrend(Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserInvestCountTrend(queryStartTime, queryEndTime, province);
    }

    @Override
    public List<KeyValueModel> queryUserInvestAmountTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserInvestAmountTrend(queryStartTime, queryEndTime, granularity, province);
    }

    @Override
    public List<KeyValueModel> queryUserAgeTrend(Date startTime, Date endTime, String province, String isInvestor){
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserAgeTrend(queryStartTime, queryEndTime, province, isInvestor);
    }
}
