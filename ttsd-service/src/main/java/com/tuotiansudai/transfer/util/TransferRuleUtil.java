package com.tuotiansudai.transfer.util;


import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;

public class TransferRuleUtil {

    public static long getTransferFee(InvestModel investModel, TransferRuleModel transferRuleModel, LoanModel loanModel) {
        double fee = getTransferFeeRate(investModel, transferRuleModel,loanModel);
        return new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(fee)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
    }

    public static double getTransferFeeRate(InvestModel investModel, TransferRuleModel transferRuleModel, LoanModel loanModel) {
        DateTime beginDate;
        DateTime endDate = new DateTime();
        if (Lists.newArrayList(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, LoanType.INVEST_INTEREST_MONTHLY_REPAY).contains(loanModel.getType())){
            beginDate = new DateTime(investModel.getCreatedTime());
        } else {
            beginDate = new DateTime(loanModel.getRecheckTime());
        }
        int days = Days.daysBetween(beginDate, endDate).getDays();
        double fee;
        if (days <= transferRuleModel.getLevelOneUpper()) {
            fee = transferRuleModel.getLevelOneFee();
        } else if (days <= transferRuleModel.getLevelTwoUpper()) {
            fee = transferRuleModel.getLevelTwoFee();
        } else {
            fee = transferRuleModel.getLevelThreeFee();
        }
        return fee;
    }


}
