package com.tuotiansudai.util;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.List;

public class InterestCalculator {

    public static int DAYS_OF_YEAR = 365;

    public static int DAYS_OF_MONTH = 30;

    public static long calculateLoanRepayInterest(LoanModel loanModel, List<InvestModel> investModels, DateTime lastRepayDate, DateTime currentRepayDate) {
        DateTime loanOutDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();

        long corpusMultiplyPeriodDays = 0;
        for (InvestModel successInvest : investModels) {
            DateTime lastInvestRepayDate = lastRepayDate;
            if (lastRepayDate.isBefore(loanOutDate) && InterestInitiateType.INTEREST_START_AT_INVEST == loanModel.getType().getInterestInitiateType()) {
                lastInvestRepayDate = new DateTime(successInvest.getCreatedTime()).withTimeAtStartOfDay().minusDays(1);
            }
            // 2015-01-01 ~ 2015-01-31: 30
            int periodDuration = Days.daysBetween(lastInvestRepayDate.withTimeAtStartOfDay(), currentRepayDate.withTimeAtStartOfDay()).getDays();
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

    public static long estimateExpectedInterest(LoanModel loanModel, long amount) {
        return InterestCalculator.calculateInterest(loanModel, amount * loanModel.getDuration());
    }

    public static long estimateCouponExpectedInterest(LoanModel loanModel, CouponModel couponModel, long amount) {
        if (loanModel == null || couponModel == null) {
            return 0;
        }

        int duration = loanModel.getDuration();

        long expectedInterest = 0;
        switch (couponModel.getCouponType()) {
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                expectedInterest = new BigDecimal(duration * couponModel.getAmount())
                        .multiply(new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate())))
                        .divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case INTEREST_COUPON:
                expectedInterest = new BigDecimal(duration * amount)
                        .multiply(new BigDecimal(couponModel.getRate()))
                        .divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case BIRTHDAY_COUPON:
                expectedInterest = new BigDecimal(30 * amount)
                        .multiply(new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate())))
                        .multiply(new BigDecimal(couponModel.getBirthdayBenefit()))
                        .divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case RED_ENVELOPE:
                expectedInterest = couponModel.getAmount();
                break;
        }
        return expectedInterest;
    }

    public static long estimateCouponExpectedFee(LoanModel loanModel, CouponModel couponModel, long amount) {
        long estimateCouponExpectedInterest = estimateCouponExpectedInterest(loanModel, couponModel, amount);
        long expectedFee = 0;
        if (Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON).contains(couponModel.getCouponType())) {
            expectedFee = new BigDecimal(estimateCouponExpectedInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        }
        return expectedFee;
    }

    public static DateTime getLastSuccessRepayDate(LoanModel loanModel, List<LoanRepayModel> loanRepayModels, final DateTime currentRepayDate) {
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1);

        Ordering<LoanRepayModel> ordering = new Ordering<LoanRepayModel>() {
            @Override
            public int compare(LoanRepayModel left, LoanRepayModel right) {
                return Ints.compare(right.getPeriod(), left.getPeriod());
            }
        };

        List<LoanRepayModel> orderingLoanRepayModels = ordering.sortedCopy(loanRepayModels);

        Optional<LoanRepayModel> optional = Iterators.tryFind(orderingLoanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                if (input.getStatus() == RepayStatus.COMPLETE) {
                    DateTime expectedRepayDate = new DateTime(input.getRepayDate());
                    DateTime actualRepayDate = new DateTime(input.getActualRepayDate());
                    return actualRepayDate.isBefore(expectedRepayDate) && actualRepayDate.isBefore(currentRepayDate);
                }
                return false;
            }
        });

        if (optional.isPresent()) {
            lastRepayDate = new DateTime(optional.get().getActualRepayDate());
        }

        return lastRepayDate;
    }

    private static long calculateInterest(LoanModel loanModel, long corpusMultiplyPeriodDays) {
        int daysOfYear = DAYS_OF_YEAR;
        new DateTime(2016, 3, 9, 21, 0, 0);
        if (loanModel.getRecheckTime() != null && loanModel.getRecheckTime().before(new DateTime(2016, 3, 9, 21, 0, 0).toDate())) {
            DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
            daysOfYear = loanDate.dayOfYear().getMaximumValue();
        }

        BigDecimal loanRate = new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate()));
        return new BigDecimal(corpusMultiplyPeriodDays).multiply(loanRate).divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN).longValue();
    }
}
