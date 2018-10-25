package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

public class InvestRepayDataItemDto {

    private long investId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    private String amount;

    private String corpus;

    private String expectedFee;

    private String actualAmount;

    private Date actualRepayDate;

    private int period;

    private String expectedInterest;

    private String actualInterest;

    private String defaultInterest;

    private String actualFee;

    private String status;

    private boolean birthdayCoupon;

    private double birthdayBenefit;

    private String couponExpectedInterest;

    private String couponActualInterest;

    private String couponDefaultInterest;

    private String overdueDay;

    private LoanModel loan;

    private String investExperienceAmount;

    private String overdueInterest;

    private String defaultFee;

    private String overdueFee;

    private String sumDefaultInterest;

    private String sumDefaultFee;

    public InvestRepayDataItemDto() {
    }

    public InvestRepayDataItemDto(InvestRepayModel model) {
        this.investId = model.getInvestId();
        this.corpus = AmountConverter.convertCentToString(model.getCorpus());
        this.expectedInterest = AmountConverter.convertCentToString(model.getExpectedInterest());
        this.expectedFee = AmountConverter.convertCentToString(model.getExpectedFee());
        this.repayDate = model.getRepayDate();
        this.period = model.getPeriod();
        this.amount = AmountConverter.convertCentToString(model.getCorpus() + model.getExpectedInterest() - model.getExpectedFee());

        if (Lists.newArrayList(RepayStatus.COMPLETE, RepayStatus.OVERDUE).contains(model.getStatus())) {
            this.actualFee = AmountConverter.convertCentToString(model.getActualFee());
            this.actualInterest = AmountConverter.convertCentToString(model.getActualInterest());
            this.actualRepayDate = model.getActualRepayDate();
        }

        if (!model.isTransferred() &&  model.getDefaultInterest() > 0) {
            this.defaultInterest = AmountConverter.convertCentToString(model.getDefaultInterest());
            this.defaultFee = AmountConverter.convertCentToString(model.getDefaultFee());
            this.sumDefaultInterest = AmountConverter.convertCentToString(model.getDefaultInterest() + model.getOverdueInterest());
            this.sumDefaultFee = AmountConverter.convertCentToString(model.getDefaultFee() + model.getOverdueFee());
        }

        if (model.getOverdueInterest() > 0) {
            this.overdueInterest = AmountConverter.convertCentToString(model.getOverdueInterest());
            this.overdueFee = AmountConverter.convertCentToString(model.getOverdueFee());
        }

        if (model.getRepayAmount() > 0) {
            this.actualAmount = AmountConverter.convertCentToString(model.getRepayAmount());
        }
        if (model.getActualRepayDate() != null) {
            this.overdueDay = String.valueOf(Days.daysBetween(new DateTime(model.getRepayDate()).withTimeAtStartOfDay(), new DateTime(model.getActualRepayDate()).withTimeAtStartOfDay()).getDays());
        }

        if (RepayStatus.COMPLETE == model.getStatus()
                && new DateTime(model.getActualRepayDate()).withTimeAtStartOfDay().isBefore((new DateTime(model.getRepayDate()).withTimeAtStartOfDay()))) {
            this.status = "提前还款";
        }
        if (RepayStatus.COMPLETE == model.getStatus() &&
                new DateTime(model.getActualRepayDate()).withTimeAtStartOfDay().isAfter((new DateTime(model.getRepayDate()).withTimeAtStartOfDay()))) {
            this.status = "逾期还款";
        }
        this.status = (RepayStatus.COMPLETE == model.getStatus() && TransferStatus.SUCCESS == model.getTransferStatus() && model.getExpectedInterest() == 0) ? model.getTransferStatus().getDescription() : model.getStatus().getDescription();
    }

    public InvestRepayDataItemDto generateInvestRepayDataItemDto(InvestRepayModel model) {
        InvestRepayDataItemDto investRepayDataItemDto = new InvestRepayDataItemDto();
        investRepayDataItemDto.setLoan(model.getLoan());
        investRepayDataItemDto.setInvestId(model.getInvestId());
        investRepayDataItemDto.setPeriod(model.getPeriod());
        investRepayDataItemDto.setActualRepayDate(model.getActualRepayDate());
        investRepayDataItemDto.setRepayDate(model.getRepayDate());
        investRepayDataItemDto.setAmount(AmountConverter.convertCentToString(model.getCorpus() + model.getExpectedInterest() - model.getExpectedFee()));
        investRepayDataItemDto.setActualAmount(AmountConverter.convertCentToString(model.getRepayAmount()));
        return investRepayDataItemDto;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(String expectedFee) {
        this.expectedFee = expectedFee;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(String defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBirthdayCoupon() {
        return birthdayCoupon;
    }

    public void setBirthdayCoupon(boolean birthdayCoupon) {
        this.birthdayCoupon = birthdayCoupon;
    }

    public double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(double birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }

    public LoanModel getLoan() {
        return loan;
    }

    public void setLoan(LoanModel loan) {
        this.loan = loan;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public String getCouponExpectedInterest() {
        return couponExpectedInterest;
    }

    public void setCouponExpectedInterest(String couponExpectedInterest) {
        this.couponExpectedInterest = couponExpectedInterest;
    }

    public String getCouponActualInterest() {
        return couponActualInterest;
    }

    public void setCouponActualInterest(String couponActualInterest) {
        this.couponActualInterest = couponActualInterest;
    }

    public String getCouponDefaultInterest() {
        return couponDefaultInterest;
    }

    public void setCouponDefaultInterest(String couponDefaultInterest) {
        this.couponDefaultInterest = couponDefaultInterest;
    }

    public String getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(String overdueDay) {
        this.overdueDay = overdueDay;
    }

    public String getInvestExperienceAmount() {
        return investExperienceAmount;
    }

    public void setInvestExperienceAmount(String investExperienceAmount) {
        this.investExperienceAmount = investExperienceAmount;
    }

    public String getDefaultFee() {
        return defaultFee;
    }

    public void setDefaultFee(String defaultFee) {
        this.defaultFee = defaultFee;
    }

    public String getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(String overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public String getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(String overdueFee) {
        this.overdueFee = overdueFee;
    }

    public String getSumDefaultInterest() {
        return sumDefaultInterest;
    }

    public void setSumDefaultInterest(String sumDefaultInterest) {
        this.sumDefaultInterest = sumDefaultInterest;
    }

    public String getSumDefaultFee() {
        return sumDefaultFee;
    }

    public void setSumDefaultFee(String sumDefaultFee) {
        this.sumDefaultFee = sumDefaultFee;
    }
}
