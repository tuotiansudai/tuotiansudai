package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class ExtraLoanRateModel implements Serializable {

    private long id;

    private long loanId;

    private long extraRateRuleId;

    private long minInvestAmount;

    private long maxInvestAmount;

    private double rate;

    private Date createdTime = new Date();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(long maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getExtraRateRuleId() {
        return extraRateRuleId;
    }

    public void setExtraRateRuleId(long extraRateRuleId) {
        this.extraRateRuleId = extraRateRuleId;
    }
}
