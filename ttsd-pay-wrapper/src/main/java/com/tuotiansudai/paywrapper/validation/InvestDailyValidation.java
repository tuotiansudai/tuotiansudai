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
public class InvestDailyValidation extends BaseDailyValidation implements DailyValidation {

    private final static Logger logger = Logger.getLogger(InvestDailyValidation.class);

    @Autowired
    public InvestDailyValidation(DailyValidationMapper dailyValidationMapper, UMPayRealTimeStatusService umPayRealTimeStatusService, UserBillMapper userBillMapper) {
        this.dailyValidationMapper = dailyValidationMapper;
        this.umPayRealTimeStatusService = umPayRealTimeStatusService;
        this.userBillMapper = userBillMapper;
    }

    @Override
    public ValidationReport validate() {
        logger.info("[Invest Daily Validation] starting...");

        List<Map<String, String>> transactions = this.dailyValidationMapper.findInvestTransactions();

        logger.info(MessageFormat.format("[Invest Daily Validation] sum is {0}", transactions.size()));

        ValidationReport validationReport = this.generateReport("03", transactions);
        validationReport.setCount(transactions.size());
        validationReport.setTitle("投资业务统计");
        validationReport.setMustacheContext("invest");

        return validationReport;
    }

    @Override
    protected boolean checkUserBill(String orderId, String amount) {
        long businessId = Long.parseLong(orderId);
        List<UserBillModel> investUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.INVEST_SUCCESS);
        List<UserBillModel> investTransferUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.INVEST_TRANSFER_IN);
        return (investUserBillModels.size() == 1 && investUserBillModels.get(0).getAmount() == Long.parseLong(amount)) ||
                (investTransferUserBillModels.size() == 1 && investTransferUserBillModels.get(0).getAmount() == Long.parseLong(amount));
    }
}
