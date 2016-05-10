package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;
import java.util.List;

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

    private LoanModel loan;

    private List<Integer> couponTypeList;

    public InvestRepayDataItemDto() {
    }

    public InvestRepayDataItemDto(InvestRepayModel model) {
        if(RepayStatus.COMPLETE == model.getStatus()) {
            this.actualFee = AmountConverter.convertCentToString(model.getActualFee());
            this.actualInterest = AmountConverter.convertCentToString(model.getActualInterest());
            this.actualRepayDate = model.getActualRepayDate();
            this.actualAmount = AmountConverter.convertCentToString(model.getCorpus() + model.getActualInterest() + model.getDefaultInterest() - model.getActualFee());
            this.defaultInterest = AmountConverter.convertCentToString(model.getDefaultInterest());
        }
        this.corpus = AmountConverter.convertCentToString(model.getCorpus());
        this.expectedFee = AmountConverter.convertCentToString(model.getExpectedFee());
        this.expectedInterest = AmountConverter.convertCentToString(model.getExpectedInterest());
        this.repayDate = model.getRepayDate();
        this.status = model.getStatus().getDescription();
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
        investRepayDataItemDto.setActualAmount(AmountConverter.convertCentToString(model.getCorpus() + model.getActualInterest() + model.getDefaultInterest() - model.getActualFee()));
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

    public List<Integer> getCouponTypeList() {
        return couponTypeList;
    }

    public void setCouponTypeList(List<Integer> couponTypeList) {
        this.couponTypeList = couponTypeList;
    }
}
