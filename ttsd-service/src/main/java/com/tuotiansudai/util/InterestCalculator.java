package com.tuotiansudai.util;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.util.Date;
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
                lastInvestRepayDate = new DateTime(successInvest.getInvestTime()).withTimeAtStartOfDay().minusDays(1);
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
            lastInvestRepayDate = new DateTime(investModel.getInvestTime()).withTimeAtStartOfDay().minusDays(1);
        }
        // 2015-01-01 ~ 2015-01-31: 30
        int periodDuration = Days.daysBetween(lastInvestRepayDate, currentRepayDate.withTimeAtStartOfDay()).getDays();
        long corpusMultiplyPeriodDays = investModel.getAmount() * periodDuration;

        return InterestCalculator.calculateInterest(loanModel, corpusMultiplyPeriodDays);
    }

    public static long estimateExpectedInterest(LoanModel loanModel, long amount) {
        return InterestCalculator.calculateInterest(loanModel, amount * loanModel.getDuration());
    }

    public static long estimateCouponRepayExpectedInterest(InvestModel investModel, LoanModel loanModel, CouponModel couponModel, DateTime currentRepayDate, DateTime lastRepayDate) {
        if (loanModel == null || couponModel == null || investModel == null) {
            return 0;
        }
        long investAmount = investModel.getAmount();
        DateTime loanOutDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        DateTime lastInvestRepayDate = lastRepayDate.withTimeAtStartOfDay();
        if (lastRepayDate.isBefore(loanOutDate) && InterestInitiateType.INTEREST_START_AT_INVEST == loanModel.getType().getInterestInitiateType()) {
            lastInvestRepayDate = new DateTime(investModel.getInvestTime()).withTimeAtStartOfDay().minusDays(1);
        }
        // 2015-01-01 ~ 2015-01-31: 30
        int periodDuration = Days.daysBetween(lastInvestRepayDate, currentRepayDate.withTimeAtStartOfDay()).getDays();

        return getCouponExpectedInterest(loanModel, couponModel, investAmount, periodDuration);
    }

    public static long getCouponExpectedInterest(LoanModel loanModel, CouponModel couponModel, long investAmount, int periodDuration) {
        long expectedInterest = 0;
        switch (couponModel.getCouponType()) {
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                expectedInterest = new BigDecimal(periodDuration * couponModel.getAmount())
                        .multiply(new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate())))
                        .divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case INTEREST_COUPON:
                expectedInterest = new BigDecimal(periodDuration * investAmount)
                        .multiply(new BigDecimal(couponModel.getRate()))
                        .divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case BIRTHDAY_COUPON:
                expectedInterest = new BigDecimal(30 * investAmount)
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

    public static long estimateCouponExpectedInterest(long investAmount, LoanModel loanModel, CouponModel couponModel) {
        if (loanModel == null || couponModel == null) {
            return 0;
        }
        int duration = loanModel.getDuration();
        return getCouponExpectedInterest(loanModel, couponModel, investAmount, duration);
    }

    public static long calculateCouponActualInterest(long investAmount, CouponModel couponModel, UserCouponModel userCouponModel, LoanModel loanModel, LoanRepayModel currentLoanRepayModel, List<LoanRepayModel> loanRepayModels) {
        DateTime currentRepayDate = new DateTime(currentLoanRepayModel.getActualRepayDate());

        LoanRepayModel lastLoanRepayModel = null;
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getPeriod() < currentLoanRepayModel.getPeriod() && loanRepayModel.getStatus() == RepayStatus.COMPLETE && loanRepayModel.getActualRepayDate().before(currentLoanRepayModel.getActualRepayDate())) {
                lastLoanRepayModel = loanRepayModel;
            }
        }

        DateTime lastRepayDate = new DateTime(loanModel.getType().getInterestInitiateType() == InterestInitiateType.INTEREST_START_AT_INVEST ? userCouponModel.getUsedTime() : loanModel.getRecheckTime()).minusDays(1);
        if (lastLoanRepayModel != null) {
            lastRepayDate = new DateTime(lastLoanRepayModel.getActualRepayDate());
        }

        int periodDuration = Days.daysBetween(lastRepayDate.withTimeAtStartOfDay(), currentRepayDate.withTimeAtStartOfDay()).getDays();

        long expectedInterest = 0;
        switch (couponModel.getCouponType()) {
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                expectedInterest = new BigDecimal(periodDuration * couponModel.getAmount())
                        .multiply(new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate())))
                        .divide(new BigDecimal(InterestCalculator.DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case INTEREST_COUPON:
                expectedInterest = new BigDecimal(periodDuration * investAmount)
                        .multiply(new BigDecimal(couponModel.getRate()))
                        .divide(new BigDecimal(InterestCalculator.DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case BIRTHDAY_COUPON:
                if (lastLoanRepayModel != null) {
                    return -1;
                }

                DateTime theFirstRepayDate = new DateTime(Iterators.tryFind(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
                    @Override
                    public boolean apply(LoanRepayModel input) {
                        return input.getPeriod() == 1;
                    }
                }).get().getRepayDate());

                periodDuration = Days.daysBetween(lastRepayDate.withTimeAtStartOfDay(), theFirstRepayDate.withTimeAtStartOfDay()).getDays();

                expectedInterest = new BigDecimal(periodDuration * investAmount)
                        .multiply(new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate())))
                        .multiply(new BigDecimal(couponModel.getBirthdayBenefit()))
                        .divide(new BigDecimal(InterestCalculator.DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
        }

        return expectedInterest;
    }

    public static long estimateCouponExpectedFee(LoanModel loanModel, CouponModel couponModel, long amount, double investFeeRate) {
        long estimateCouponExpectedInterest = estimateCouponExpectedInterest(amount, loanModel, couponModel);
        long expectedFee = 0;
        if (Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.BIRTHDAY_COUPON).contains(couponModel.getCouponType())) {
            expectedFee = new BigDecimal(estimateCouponExpectedInterest).multiply(new BigDecimal(investFeeRate)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
        }
        return expectedFee;
    }

    public static DateTime getLastSuccessRepayDate(LoanModel loanModel, List<LoanRepayModel> loanRepayModels) {
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1);

        Ordering<LoanRepayModel> orderingByPeriodDesc = new Ordering<LoanRepayModel>() {
            @Override
            public int compare(LoanRepayModel left, LoanRepayModel right) {
                return Ints.compare(right.getPeriod(), left.getPeriod());
            }
        };

        List<LoanRepayModel> orderingLoanRepayModels = orderingByPeriodDesc.sortedCopy(loanRepayModels);

        Optional<LoanRepayModel> optional = Iterators.tryFind(orderingLoanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.COMPLETE;
            }
        });

        if (optional.isPresent()) {
            lastRepayDate = new DateTime(optional.get().getActualRepayDate());
        }

        return lastRepayDate;
    }

    public static long calculateInterest(LoanModel loanModel, long corpusMultiplyPeriodDays) {
        int daysOfYear = DAYS_OF_YEAR;

        //2016-03-09 放款后标的按每期30天一年365天计算利息
        if (loanModel.getRecheckTime() != null && loanModel.getRecheckTime().before(new DateTime(2016, 3, 9, 21, 0, 0).toDate())) {
            DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
            daysOfYear = loanDate.dayOfYear().getMaximumValue();
        }

        BigDecimal loanRate = new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate()));
        return new BigDecimal(corpusMultiplyPeriodDays).multiply(loanRate).divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN).longValue();
    }

    public static long calculateTransferInterest(TransferApplicationModel transferApplicationModel, List<InvestRepayModel> investRepayModels) {
        long totalExpectedInterestAmount = 0;
        for (int i = transferApplicationModel.getPeriod() - 1; i < investRepayModels.size(); i++) {
            totalExpectedInterestAmount += investRepayModels.get(i).getExpectedInterest() - investRepayModels.get(i).getExpectedFee();
        }
        if(transferApplicationModel.getInvestAmount() != transferApplicationModel.getTransferAmount()){
            totalExpectedInterestAmount += transferApplicationModel.getInvestAmount() - transferApplicationModel.getTransferAmount();
        }
        return totalExpectedInterestAmount;
    }

    public static long calculateTransferInterest(TransferApplicationModel transferApplicationModel, List<InvestRepayModel> investRepayModels,double fee) {
        long totalExpectedInterestAmount = 0;
        for (int i = transferApplicationModel.getPeriod() - 1; i < investRepayModels.size(); i++) {
            totalExpectedInterestAmount += investRepayModels.get(i).getExpectedInterest();
        }
        return totalExpectedInterestAmount - new BigDecimal(totalExpectedInterestAmount).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(fee)).longValue();
    }

    public static long calculateExtraLoanRateInterest(LoanModel loanModel, double extraRate, InvestModel investModel, Date endTime) {
        DateTime startTime;
        if (InterestInitiateType.INTEREST_START_AT_INVEST == loanModel.getType().getInterestInitiateType()) {
            startTime = new DateTime(investModel.getInvestTime()).withTimeAtStartOfDay().minusDays(1);
        } else {
            startTime = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        }
        int periodDuration = Days.daysBetween(startTime, new DateTime(endTime).withTimeAtStartOfDay()).getDays();
        return new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(extraRate)).multiply(new BigDecimal(periodDuration)).
                divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
    }

    public static long calculateExtraLoanRateExpectedInterest(double extraRate, long amount, int periodDuration, double investFeeRate) {
        return new BigDecimal(amount).multiply(new BigDecimal(extraRate)).multiply(new BigDecimal(periodDuration)).
                divide(new BigDecimal(DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN).longValue();
    }

}
