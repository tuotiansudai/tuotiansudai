package com.tuotiansudai.utils;

import com.tuotiansudai.repository.model.LoanModel;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class InterestCalculator {

    public static long calculateInterest(LoanModel loanModel, long corpusMultiplyPeriodDays) {
        BigDecimal loanRate = new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate()));

        DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        int daysOfYear = loanDate.dayOfYear().getMaximumValue();
        return new BigDecimal(corpusMultiplyPeriodDays).multiply(loanRate).divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN).longValue();
    }
}
