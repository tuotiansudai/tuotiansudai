package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.repository.model.CouponType;

import java.io.Serializable;
import java.util.Date;

public class CouponUseRecordView implements Serializable {

    private long id;

    private long loanId;

    private String loanName;

    private CouponType couponType;

    private long investAmount;

    private String investAmountStr;

    private String rate;

    private long couponAmount;

    private String couponAmountStr;

    private Date usedTime;

    private long expectInterest;

    private String expectInterestStr;

    private long expectedFee;

    private String expectedFeeStr;

    private long expectedIncome;

    private String expectedIncomeStr;

    private String birthdayBenefit;

    public CouponUseRecordView() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponType.getName();
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public long getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(long couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponAmountStr() {
        return couponAmountStr;
    }

    public void setCouponAmountStr(String couponAmountStr) {
        this.couponAmountStr = couponAmountStr;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public long getExpectInterest() {
        return expectInterest;
    }

    public void setExpectInterest(long expectInterest) {
        this.expectInterest = expectInterest;
    }

    public String getExpectInterestStr() {
        return expectInterestStr;
    }

    public void setExpectInterestStr(String expectInterestStr) {
        this.expectInterestStr = expectInterestStr;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestAmountStr() {
        return investAmountStr;
    }

    public void setInvestAmountStr(String investAmountStr) {
        this.investAmountStr = investAmountStr;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public long getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(long expectedFee) {
        this.expectedFee = expectedFee;
    }

    public String getExpectedFeeStr() {
        return expectedFeeStr;
    }

    public void setExpectedFeeStr(String expectedFeeStr) {
        this.expectedFeeStr = expectedFeeStr;
    }

    public long getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(long expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

    public String getExpectedIncomeStr() {
        return expectedIncomeStr;
    }

    public void setExpectedIncomeStr(String expectedIncomeStr) {
        this.expectedIncomeStr = expectedIncomeStr;
    }

    public String getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(String birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }
}
