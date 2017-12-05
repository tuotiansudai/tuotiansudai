package com.tuotiansudai.paywrapper.validation;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.paywrapper.repository.mapper.DailyValidationMapper;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


@Component
public class ReferrerRewardDailyValidation extends BaseDailyValidation implements DailyValidation {

    private final static Logger logger = Logger.getLogger(ReferrerRewardDailyValidation.class);

    @Autowired
    public ReferrerRewardDailyValidation(DailyValidationMapper dailyValidationMapper, UMPayRealTimeStatusService umPayRealTimeStatusService, UserBillMapper userBillMapper) {
        this.dailyValidationMapper = dailyValidationMapper;
        this.umPayRealTimeStatusService = umPayRealTimeStatusService;
        this.userBillMapper = userBillMapper;
    }

    @Override
    public ValidationReport validate() {
        logger.info("[Referrer Reward Validation] starting...");

        List<Map<String, String>> transactions = this.dailyValidationMapper.findReferrerRewardTransactions();

        logger.info(MessageFormat.format("[Referrer Reward Validation] sum is {0}", transactions.size()));

        ValidationReport validationReport = this.generateReport("04", transactions);
        validationReport.setCount(transactions.size());
        validationReport.setTitle("推荐奖励业务统计");
        validationReport.setMustacheContext("referrerReward");

        return validationReport;
    }

    @Override
    protected boolean checkUserBill(String orderId, String amount) {
        long businessId = Long.parseLong(orderId);
        List<UserBillModel> referrerRewardBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.REFERRER_REWARD);
        return referrerRewardBillModels.size() == 1 && referrerRewardBillModels.get(0).getAmount() == Long.parseLong(amount);
    }
}
