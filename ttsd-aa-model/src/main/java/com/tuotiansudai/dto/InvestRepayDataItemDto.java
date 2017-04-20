package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String couponDefaultInterest;

    private String overdueDay;

    private LoanModel loan;

    private String investExperienceAmount;

    public InvestRepayDataItemDto() {
    }

    public InvestRepayDataItemDto(InvestRepayModel model) {
        if(RepayStatus.COMPLETE == model.getStatus() || RepayStatus.OVERDUE == model.getStatus()) {
            this.actualFee = AmountConverter.convertCentToString(model.getActualFee());
            this.actualInterest = AmountConverter.convertCentToString(model.getActualInterest());
            this.actualRepayDate = model.getActualRepayDate();
            if(model.getRepayAmount() > 0) this.actualAmount = AmountConverter.convertCentToString(model.getRepayAmount());
            if((RepayStatus.OVERDUE == model.getStatus() || RepayStatus.COMPLETE == model.getStatus()) && model.getActualRepayDate() != null) {
                this.overdueDay = String.valueOf(Days.daysBetween(new DateTime(model.getRepayDate()).withTimeAtStartOfDay(), new DateTime(model.getActualRepayDate()).withTimeAtStartOfDay()).getDays());
            }
        }
        this.investId = model.getInvestId();
        if(model.getDefaultInterest() > 0) this.defaultInterest = AmountConverter.convertCentToString(model.getDefaultInterest());
        if(model.getExpectedFee() > 0) this.expectedFee = AmountConverter.convertCentToString(model.getExpectedFee());
        this.corpus = AmountConverter.convertCentToString(model.getCorpus());
        this.expectedInterest = AmountConverter.convertCentToString(model.getExpectedInterest());
        this.repayDate = model.getRepayDate();
        if(RepayStatus.COMPLETE == model.getStatus() && model.getActualRepayDate() != null && model.getActualRepayDate().before(new DateTime(model.getRepayDate()).withTimeAtStartOfDay().toDate())){
            this.status = "提前还款";
        }else if(RepayStatus.OVERDUE == model.getStatus()){
            this.status = "逾期还款";
        }else{
            this.status = (RepayStatus.COMPLETE == model.getStatus() && TransferStatus.SUCCESS == model.getTransferStatus() && model.getExpectedInterest() == 0 )?model.getTransferStatus().getDescription():model.getStatus().getDescription();
        }
        this.period = model.getPeriod();
        this.amount = AmountConverter.convertCentToString(model.getCorpus() + model.getExpectedInterest() - model.getExpectedFee());
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
}
