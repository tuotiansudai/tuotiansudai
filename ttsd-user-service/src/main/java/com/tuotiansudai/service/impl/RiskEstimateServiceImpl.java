package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.repository.mapper.RiskEstimateMapper;
import com.tuotiansudai.repository.model.RiskEstimateModel;
import com.tuotiansudai.service.ExperienceBillService;
import com.tuotiansudai.service.RiskEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskEstimateServiceImpl implements RiskEstimateService {

    private static final String ESTIMATE_ALERT_REDIS_KEY = "estimate:alert";

    private final RiskEstimateMapper riskEstimateMapper;

    private final ExperienceBillService experienceBillService;

    private final RedisWrapperClient redisWrapperClient;

    @Autowired
    public RiskEstimateServiceImpl(RiskEstimateMapper riskEstimateMapper, ExperienceBillService experienceBillService, RedisWrapperClient redisWrapperClient) {
        this.riskEstimateMapper = riskEstimateMapper;
        this.experienceBillService = experienceBillService;
        this.redisWrapperClient = redisWrapperClient;
    }

    @Override
    public Estimate getEstimate(String loginName) {
        RiskEstimateModel estimateModel = riskEstimateMapper.findByLoginName(loginName);
        return estimateModel != null ? estimateModel.getEstimate() : null;
    }

    @Override
    public Estimate estimate(String loginName, List<Integer> answers) {
        if (answers == null || answers.size() != 8) {
            return null;
        }

        RiskEstimateModel model = new RiskEstimateModel(loginName, answers);
        if (riskEstimateMapper.findByLoginName(loginName) == null) {
            riskEstimateMapper.create(model);
            experienceBillService.updateUserExperienceBalanceByLoginName(100000, loginName, ExperienceBillOperationType.IN, ExperienceBillBusinessType.RISK_ESTIMATE);
        } else {
            riskEstimateMapper.update(model);
        }

        return model.getEstimate();
    }

    @Override
    public boolean alertEstimate(String loginName) {
        if (riskEstimateMapper.findByLoginName(loginName) != null) {
            return false;
        }
        if (Strings.isNullOrEmpty(redisWrapperClient.hget(ESTIMATE_ALERT_REDIS_KEY, loginName))) {
            redisWrapperClient.hset(ESTIMATE_ALERT_REDIS_KEY, loginName, "1");
            return true;
        }

        return false;
    }
}
