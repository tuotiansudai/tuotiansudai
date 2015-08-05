package com.esoft.archer.user.model;

/**
 * Created by Administrator on 2015/8/4.
 */
public class ReferrerInvest {

    private String investUserId;
    private int level;
    private double investMoney;
    private String investTime;
    private double rewardMoney;
    private double rewardRate;
    private String rewardTime;

    public String getInvestUserId() {
        return investUserId;
    }

    public void setInvestUserId(String investUserId) {
        this.investUserId = investUserId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(double investMoney) {
        this.investMoney = investMoney;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public double getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(double rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public double getRewardRate() {
        return rewardRate;
    }

    public void setRewardRate(double rewardRate) {
        this.rewardRate = rewardRate;
    }

    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }
}