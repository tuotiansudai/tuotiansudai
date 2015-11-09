package com.esoft.jdp2p.invest.controller;

import java.util.Date;

public class InvestItem implements java.io.Serializable {
    private String loanId;
    private String loanName;
    private int loanDeadline;
    private String investorId;
    private String investorName;
    private String referrerId;
    private String referrerName;
    private Boolean isMerchandiser;
    private Integer refereeLevel;
    private Double reward;
    private String rewardStatus;
    private Date rewardTime;
    private Date investTime;
    private Double money;
    private String source;

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

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public int getLoanDeadline() {
        return loanDeadline;
    }

    public void setLoanDeadline(int loanDeadline) {
        this.loanDeadline = loanDeadline;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public Boolean getIsMerchandiser() {
        return isMerchandiser;
    }

    public void setIsMerchandiser(Boolean isMerchandiser) {
        this.isMerchandiser = isMerchandiser;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
