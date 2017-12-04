package com.tuotiansudai.paywrapper.validation;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.paywrapper.repository.mapper.DailyValidationMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferSearchResponseModel;
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

    public ValidationReport validate() {
        logger.info("[Invest Repay Daily Validation] starting...");

        List<Map<String, String>> transactions = this.dailyValidationMapper.findInvestRepayTransactions();

        logger.info(MessageFormat.format("[Invest Repay Daily Validation] sum is {0}", transactions.size()));

        ValidationReport report = new ValidationReport("investRepay", "还款业务统计", transactions.size());

        for (Map<String, String> transaction : transactions) {
            String orderId = transaction.get("order_id");
            String amount = transaction.get("amount");
            String merDate = transaction.get("mer_date");
            try {
                TransferSearchResponseModel transferStatus = umPayRealTimeStatusService.getTransferStatus(orderId, merDate, "03"/*标的转账*/);
                this.addSummary(report, transferStatus.getRetCode());

                if (transferStatus.isSuccess()) {
                    if (!checkUserBill(orderId, amount)) {
                        this.addIssue(report, orderId, "用户交易记录异常");
                    }
                } else {
                    this.addIssue(report, orderId, transferStatus.getRetMsg());
                }
            } catch (Exception e) {
                this.addIssue(report, orderId, "查询失败");
                this.addSummary(report, "exception");
                logger.warn(MessageFormat.format("[Invest Repay Daily Validation] query status failed, order id is {0}", orderId), e);
            }
        }
        return report;
    }

    private boolean checkUserBill(String orderId, String amount) {
        long businessId = Long.parseLong(orderId.split("X")[0]);
        List<UserBillModel> userBillModels = userBillMapper.findByOrderId(businessId);
        long userBillAmount = 0;
        for (UserBillModel userBillModel : userBillModels) {
            if (userBillModel.getBusinessType() == UserBillBusinessType.NORMAL_REPAY && userBillModel.getOperationType() == UserBillOperationType.TI_BALANCE) {
                userBillAmount += userBillModel.getAmount();
                continue;
            }
            if (userBillModel.getBusinessType() == UserBillBusinessType.ADVANCE_REPAY && userBillModel.getOperationType() == UserBillOperationType.TI_BALANCE) {
                userBillAmount += userBillModel.getAmount();
                continue;
            }
            if (userBillModel.getBusinessType() == UserBillBusinessType.INVEST_FEE && userBillModel.getOperationType() == UserBillOperationType.TO_BALANCE) {
                userBillAmount -= userBillModel.getAmount();
                continue;
            }
            return false;
        }
        return userBillAmount == Long.parseLong(amount);
    }
}
