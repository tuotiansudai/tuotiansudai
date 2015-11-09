package com.tuotiansudai.utils;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.List;

public class InterestCalculator {

    public static long calculateInterest(LoanModel loanModel, long corpusMultiplyPeriodDays) {
        BigDecimal loanRate = new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate()));

        DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        int daysOfYear = loanDate.dayOfYear().getMaximumValue();
        return new BigDecimal(corpusMultiplyPeriodDays).multiply(loanRate).divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN).longValue();
    }

    public static long calculateLoanRepayInterest(LoanModel loanModel, List<InvestModel> investModels, DateTime lastRepayDate, DateTime currentRepayDate) {
        DateTime loanOutDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();

        long corpusMultiplyPeriodDays = 0;
        for (InvestModel successInvest : investModels) {
            DateTime lastInvestRepayDate = lastRepayDate;
            if (lastRepayDate.isBefore(loanOutDate) && InterestInitiateType.INTEREST_START_AT_INVEST == loanModel.getType().getInterestInitiateType()) {
                lastInvestRepayDate = new DateTime(successInvest.getCreatedTime()).withTimeAtStartOfDay().minusDays(1);
            }
            // 2015-01-01 ~ 2015-01-31: 30
            int periodDuration = Days.daysBetween(lastInvestRepayDate, currentRepayDate.withTimeAtStartOfDay()).getDays();
            corpusMultiplyPeriodDays += successInvest.getAmount() * periodDuration;
        }

        return InterestCalculator.calculateInterest(loanModel, corpusMultiplyPeriodDays);
    }

    public static long calculateInvestRepayInterest(LoanModel loanModel, InvestModel investModel, DateTime lastRepayDate, DateTime currentRepayDate) {
        DateTime loanOutDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();

        DateTime lastInvestRepayDate = lastRepayDate.withTimeAtStartOfDay();
        if (lastRepayDate.isBefore(loanOutDate) && InterestInitiateType.INTEREST_START_AT_INVEST == loanModel.getType().getInterestInitiateType()) {
            lastInvestRepayDate = new DateTime(investModel.getCreatedTime()).withTimeAtStartOfDay().minusDays(1);
        }
        // 2015-01-01 ~ 2015-01-31: 30
        int periodDuration = Days.daysBetween(lastInvestRepayDate, currentRepayDate.withTimeAtStartOfDay()).getDays();
        long corpusMultiplyPeriodDays = investModel.getAmount() * periodDuration;

        return InterestCalculator.calculateInterest(loanModel, corpusMultiplyPeriodDays);
    }

    public static DateTime getLastSuccessRepayDate(LoanModel loanModel, List<LoanRepayModel> loanRepayModels, final DateTime currentRepayDate) {
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();

        Optional<LoanRepayModel> optional = Iterators.tryFind(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.COMPLETE && new DateTime(input.getActualRepayDate()).withTimeAtStartOfDay().getMillis() <= currentRepayDate.withTimeAtStartOfDay().getMillis();
            }
        });

        if (optional.isPresent()) {
            lastRepayDate = new DateTime(optional.get().getActualRepayDate()).withTimeAtStartOfDay();
        }

        return lastRepayDate;
    }
}
