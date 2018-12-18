package com.tuotiansudai.paywrapper.validation;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.paywrapper.repository.mapper.DailyValidationMapper;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


@Component
public class InvestRepayDailyValidation extends BaseDailyValidation implements DailyValidation {

    private final static Logger logger = Logger.getLogger(InvestRepayDailyValidation.class);

    @Autowired
    public InvestRepayDailyValidation(DailyValidationMapper dailyValidationMapper, UMPayRealTimeStatusService umPayRealTimeStatusService, UserBillMapper userBillMapper) {
        this.dailyValidationMapper = dailyValidationMapper;
        this.umPayRealTimeStatusService = umPayRealTimeStatusService;
        this.userBillMapper = userBillMapper;
    }

    @Override
    public ValidationReport validate() {
        logger.info("[Invest Repay Daily Validation] starting...");

        List<Map<String, String>> transactions = this.dailyValidationMapper.findInvestRepayTransactions();

        logger.info(MessageFormat.format("[Invest Repay Daily Validation] sum is {0}", transactions.size()));

        ValidationReport validationReport = this.generateReport("03", transactions);
        validationReport.setCount(transactions.size());
        validationReport.setTitle("还款业务统计");
        validationReport.setMustacheContext("investRepay");

        return validationReport;
    }

    @Override
    protected boolean checkUserBill(String orderId, String amount) {
        long businessId = Long.parseLong(orderId.split("X")[0]);
        List<UserBillModel> normalRepayUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.NORMAL_REPAY);
        List<UserBillModel> advancedRepayUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.ADVANCE_REPAY);
        List<UserBillModel> investFeeRepayUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.INVEST_FEE);
        List<UserBillModel> overdueRepayUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.OVERDUE_REPAY);
        List<UserBillModel> overdueInterestUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.OVERDUE_INTEREST);
        List<UserBillModel> overdueFeeUserBillModels = userBillMapper.findByOrderIdAndBusinessType(businessId, UserBillBusinessType.OVERDUE_INTEREST_FEE);
        long userBillAmount = 0;
        if (normalRepayUserBillModels.size() == 1) {
            userBillAmount += normalRepayUserBillModels.get(0).getAmount();
        }
        if (advancedRepayUserBillModels.size() == 1) {
            userBillAmount += advancedRepayUserBillModels.get(0).getAmount();
        }
        if (overdueRepayUserBillModels.size() == 1) {
            userBillAmount += overdueRepayUserBillModels.get(0).getAmount();
        }
        if (overdueInterestUserBillModels.size() == 1) {
            userBillAmount += overdueInterestUserBillModels.get(0).getAmount();
        }
        if (investFeeRepayUserBillModels.size() == 1) {
            userBillAmount -= investFeeRepayUserBillModels.get(0).getAmount();
        }
        if (overdueFeeUserBillModels.size() == 1) {
            userBillAmount -= overdueFeeUserBillModels.get(0).getAmount();
        }
        return userBillAmount == Long.parseLong(amount);
    }
}
