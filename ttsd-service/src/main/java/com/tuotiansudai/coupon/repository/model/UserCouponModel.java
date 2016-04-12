package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.repository.model.InvestStatus;

import java.io.Serializable;
import java.util.Date;

public class UserCouponModel implements Serializable{

    private long id;

    private long couponId;

    private String loginName;

    private Long loanId;

    private Long investId;

    private Date usedTime;

    private long expectedInterest;

    private long actualInterest;

    private long defaultInterest;

    private long expectedFee;

    private long actualFee;

    private Date startTime;

    private Date endTime;

    private Date createdTime;

    private InvestStatus status;

    private String loanName;

    private Long investAmount;

    public UserCouponModel() {
    }

    public UserCouponModel(String loginName, long couponId, Date startTime, Date endTime) {
        this.loginName = loginName;
        this.couponId = couponId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(long expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(long defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public long getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(long expectedFee) {
        this.expectedFee = expectedFee;
    }

    public long getActualFee() {
        return actualFee;
    }

    public void setActualFee(long actualFee) {
        this.actualFee = actualFee;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public InvestStatus getStatus() {
        return status;
    }

    public void setStatus(InvestStatus status) {
        this.status = status;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public Long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Long investAmount) {
        this.investAmount = investAmount;
    }
}
