package com.esoft.jdp2p.invest.controller;

import java.util.Date;

public class InvestItem implements java.io.Serializable {
    private String loanId;
    private String loanName;
    private String loadType;
    private String investorId;
    private String investorName;
    private String referrerId;
    private String referrerName;

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    private Integer refereeLevel;
    private Double reward;
    private Date rewardTime;
    private Date investTime;
    private boolean isAutoInvest;
    private Double money;
    private String loanStatus;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getInvestorId() {
        return investorId;
    }

    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public Integer getRefereeLevel() {
        return refereeLevel;
    }

    public Date getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(Date rewardTime) {
        this.rewardTime = rewardTime;
    }

    public void setRefereeLevel(Integer refereeLevel) {
        this.refereeLevel = refereeLevel;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public boolean getIsAutoInvest() {
        return isAutoInvest;
    }

    public void setIsAutoInvest(boolean isAutoInvest) {
        this.isAutoInvest = isAutoInvest;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
}
