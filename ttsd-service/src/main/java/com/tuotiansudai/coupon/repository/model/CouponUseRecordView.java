package com.tuotiansudai.coupon.repository.model;

import java.util.Date;

public class CouponUseRecordView {

    private long loanId;

    private String loanName;

    private String couponName;

    private long couponAmount;

    private String couponAmountStr;

    private Date usedTime;

    private long expectInterest;

    private String expectInterestStr;

    public CouponUseRecordView() {
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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
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
}
