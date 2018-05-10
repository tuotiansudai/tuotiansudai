package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class SuperScholarRewardView implements Serializable{

    private String amount;
    private String annualizedAmount;
    private String rewardRate;
    private String reward;
    private Date investTime;

    public SuperScholarRewardView() {
    }

    public SuperScholarRewardView(String amount, String annualizedAmount, String rewardRate, String reward, Date investTime) {
        this.amount = amount;
        this.annualizedAmount = annualizedAmount;
        this.rewardRate = rewardRate;
        this.reward = reward;
        this.investTime = investTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAnnualizedAmount() {
        return annualizedAmount;
    }

    public void setAnnualizedAmount(String annualizedAmount) {
        this.annualizedAmount = annualizedAmount;
    }

    public String getRewardRate() {
        return rewardRate;
    }

    public void setRewardRate(String rewardRate) {
        this.rewardRate = rewardRate;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }
}
