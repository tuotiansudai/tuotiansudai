package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.repository.model.CouponType;

import java.io.Serializable;
import java.util.Date;

public class CouponUseRecordView implements Serializable {

    private long loanId;

    private String loanName;

    private CouponType couponType;

    private long couponAmount;

    private String couponAmountStr;

    private Date usedTime;

    private long expectInterest;

    private String expectInterestStr;

    public CouponUseRecordView() {
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
}
