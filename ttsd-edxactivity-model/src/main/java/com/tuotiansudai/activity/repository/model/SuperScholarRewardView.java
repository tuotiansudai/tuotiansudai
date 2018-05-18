package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class SuperScholarRewardView implements Serializable{

    private String userName;
    private String mobile;
    private String amount;
    private String annualizedAmount;
    private String rewardRate;
    private String reward;
    private boolean isCashBack;
    private Date investTime;

    public SuperScholarRewardView() {
    }

    public SuperScholarRewardView(String userName, String mobile, String amount, String annualizedAmount, String rewardRate, String reward, boolean isCashBack, Date investTime) {
        this.userName = userName;
        this.mobile = mobile;
        this.amount = amount;
        this.annualizedAmount = annualizedAmount;
        this.rewardRate = rewardRate;
        this.reward = reward;
        this.isCashBack = isCashBack;
        this.investTime = investTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public boolean isCashBack() {
        return isCashBack;
    }

    public void setCashBack(boolean cashBack) {
        isCashBack = cashBack;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }
}
