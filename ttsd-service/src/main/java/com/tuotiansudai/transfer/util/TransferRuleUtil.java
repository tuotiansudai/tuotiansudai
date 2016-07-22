package com.tuotiansudai.transfer.util;


import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.Date;

public class TransferRuleUtil {

    public static int getInvestHoldDays(LoanType loanType, Date loanRecheckTime, Date investTime) {
        DateTime beginDate =  new DateTime(LoanType.LOAN_INTEREST_MONTHLY_REPAY == loanType ? loanRecheckTime : investTime);
        return Days.daysBetween(beginDate, new DateTime()).getDays() + 1;
    }

    public static long getTransferFee(LoanType loanType, Date loanRecheckTime, long investAmount, Date investTime, TransferRuleModel transferRuleModel) {
        double fee = TransferRuleUtil.getTransferFeeRate(loanType, loanRecheckTime, investTime, transferRuleModel);
        return new BigDecimal(investAmount).multiply(new BigDecimal(fee)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
    }

    public static double getTransferFeeRate(LoanType loanType, Date loanRecheckTime, Date investTime, TransferRuleModel transferRuleModel) {
        int investHoldDays = TransferRuleUtil.getInvestHoldDays(loanType, loanRecheckTime, investTime);

        double fee = transferRuleModel.getLevelThreeFee();
        if (investHoldDays <= transferRuleModel.getLevelTwoUpper()) {
            fee = transferRuleModel.getLevelTwoFee();
        }

        if (investHoldDays <= transferRuleModel.getLevelOneUpper()) {
            fee = transferRuleModel.getLevelOneFee();
        }

        return fee;
    }
}
