package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserFundView implements Serializable {

    private long balance; //可用余额(分)

    private long totalIncome; //累计收益=已收出借收益+已收出借奖励(阶梯加息)+已收红包奖励+已收推荐奖励
    private long actualTotalInterest; //已收出借收益(分)=出借本金收益+加息券收益
    private long actualTotalExtraInterest; //已收出借奖励(阶梯加息)(分)
    private long actualCouponInterest;//已收优惠券奖励(分)
    private long referRewardAmount; //已收推荐奖励(分)
    private long redEnvelopeAmount; //已收红包奖励(分)

    private long expectedTotalCorpusInterest; //待收回款=待收出借本金+待收出借收益+待收出借奖励(阶梯加息)------已放款后的标的包含加息券，出借奖励，活动加息所产生的利息
    private long expectedTotalCorpus; //待收出借本金(分)
    private long expectedTotalInterest; //待收出借收益(分)
    private long expectedCouponInterest;//待收优惠券奖励(分)
    private long expectedTotalExtraInterest; //待收出借奖励(阶梯加息)(分)
    private long overdueTotalCorpusInterest; //逾期待收回款=逾期出借本金+逾期出借本金收益+逾期罚息收益
    private long overdueTotalCorpus; //逾期待收本金
    private long overdueTotalInterest; //逾期待收本金收益
    private long overdueTotalDefaultInterest; //逾期待收罚息收益

    private long investFrozeAmount; //出借冻结资金(分)
    private long withdrawFrozeAmount; //提现冻结资金(分)

    private long currentMonthInvestRepayCount; //本月回款笔数

    private long expectedExperienceInterest;//未收体验金收益
    private long actualExperienceInterest;//已收体验金收益

    private long investAmount; //直投金额

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public long getActualTotalInterest() {
        return actualTotalInterest;
    }

    public void setActualTotalInterest(long actualTotalInterest) {
        this.actualTotalInterest = actualTotalInterest;
    }

    public long getActualTotalExtraInterest() {
        return actualTotalExtraInterest;
    }

    public void setActualTotalExtraInterest(long actualTotalExtraInterest) {
        this.actualTotalExtraInterest = actualTotalExtraInterest;
    }

    public long getReferRewardAmount() {
        return referRewardAmount;
    }

    public void setReferRewardAmount(long referRewardAmount) {
        this.referRewardAmount = referRewardAmount;
    }

    public long getRedEnvelopeAmount() {
        return redEnvelopeAmount;
    }

    public void setRedEnvelopeAmount(long redEnvelopeAmount) {
        this.redEnvelopeAmount = redEnvelopeAmount;
    }

    public long getExpectedTotalCorpusInterest() {
        return expectedTotalCorpusInterest;
    }

    public void setExpectedTotalCorpusInterest(long expectedTotalCorpusInterest) {
        this.expectedTotalCorpusInterest = expectedTotalCorpusInterest;
    }

    public long getExpectedTotalCorpus() {
        return expectedTotalCorpus;
    }

    public void setExpectedTotalCorpus(long expectedTotalCorpus) {
        this.expectedTotalCorpus = expectedTotalCorpus;
    }

    public long getExpectedTotalInterest() {
        return expectedTotalInterest;
    }

    public void setExpectedTotalInterest(long expectedTotalInterest) {
        this.expectedTotalInterest = expectedTotalInterest;
    }

    public long getExpectedTotalExtraInterest() {
        return expectedTotalExtraInterest;
    }

    public void setExpectedTotalExtraInterest(long expectedTotalExtraInterest) {
        this.expectedTotalExtraInterest = expectedTotalExtraInterest;
    }

    public long getInvestFrozeAmount() {
        return investFrozeAmount;
    }

    public void setInvestFrozeAmount(long investFrozeAmount) {
        this.investFrozeAmount = investFrozeAmount;
    }

    public long getWithdrawFrozeAmount() {
        return withdrawFrozeAmount;
    }

    public void setWithdrawFrozeAmount(long withdrawFrozeAmount) {
        this.withdrawFrozeAmount = withdrawFrozeAmount;
    }

    public long getCurrentMonthInvestRepayCount() {
        return currentMonthInvestRepayCount;
    }

    public void setCurrentMonthInvestRepayCount(long currentMonthInvestRepayCount) {
        this.currentMonthInvestRepayCount = currentMonthInvestRepayCount;
    }

    public long getExpectedExperienceInterest() {
        return expectedExperienceInterest;
    }

    public void setExpectedExperienceInterest(long expectedExperienceInterest) {
        this.expectedExperienceInterest = expectedExperienceInterest;
    }

    public long getActualExperienceInterest() {
        return actualExperienceInterest;
    }

    public void setActualExperienceInterest(long actualExperienceInterest) {
        this.actualExperienceInterest = actualExperienceInterest;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getActualCouponInterest() {
        return actualCouponInterest;
    }

    public void setActualCouponInterest(long actualCouponInterest) {
        this.actualCouponInterest = actualCouponInterest;
    }

    public long getExpectedCouponInterest() {
        return expectedCouponInterest;
    }

    public void setExpectedCouponInterest(long expectedCouponInterest) {
        this.expectedCouponInterest = expectedCouponInterest;
    }

    public long getOverdueTotalCorpusInterest() {
        return overdueTotalCorpusInterest;
    }

    public void setOverdueTotalCorpusInterest(long overdueTotalCorpusInterest) {
        this.overdueTotalCorpusInterest = overdueTotalCorpusInterest;
    }

    public long getOverdueTotalCorpus() {
        return overdueTotalCorpus;
    }

    public void setOverdueTotalCorpus(long overdueTotalCorpus) {
        this.overdueTotalCorpus = overdueTotalCorpus;
    }

    public long getOverdueTotalInterest() {
        return overdueTotalInterest;
    }

    public void setOverdueTotalInterest(long overdueTotalInterest) {
        this.overdueTotalInterest = overdueTotalInterest;
    }

    public long getOverdueTotalDefaultInterest() {
        return overdueTotalDefaultInterest;
    }

    public void setOverdueTotalDefaultInterest(long overdueTotalDefaultInterest) {
        this.overdueTotalDefaultInterest = overdueTotalDefaultInterest;
    }
}
