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
public class RedEnvelopDailyValidation extends BaseDailyValidation implements DailyValidation {

    private final static Logger logger = Logger.getLogger(RedEnvelopDailyValidation.class);

    @Autowired
    public RedEnvelopDailyValidation(DailyValidationMapper dailyValidationMapper, UMPayRealTimeStatusService umPayRealTimeStatusService, UserBillMapper userBillMapper) {
        this.dailyValidationMapper = dailyValidationMapper;
        this.umPayRealTimeStatusService = umPayRealTimeStatusService;
        this.userBillMapper = userBillMapper;
    }

    public ValidationReport validate() {
        logger.info("[Red Envelop Daily Validation] starting...");

        List<Map<String, String>> transactions = this.dailyValidationMapper.findRedEnvelopTransactions();

        logger.info(MessageFormat.format("[Red Envelop Daily Validation] sum is {0}", transactions.size()));

        ValidationReport validationReport = this.generateReport("04", transactions);
        validationReport.setCount(transactions.size());
        validationReport.setTitle("投资红包业务统计");
        validationReport.setMustacheContext("redEnvelop");

        return validationReport;
    }

    @Override
    protected boolean checkUserBill(String orderId, String amount) {
        long businessId = Long.parseLong(orderId.split("X")[0]);
        List<UserBillModel> redEnvelopUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.RED_ENVELOPE);
        return redEnvelopUserBillModels.size() ==  1 && redEnvelopUserBillModels.get(0).getAmount() == Long.parseLong(amount);
    }
}
