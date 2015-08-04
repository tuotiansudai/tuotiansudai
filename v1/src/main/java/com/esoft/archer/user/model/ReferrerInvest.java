package com.esoft.archer.user.model;

import java.util.Date;

/**
 * Created by Administrator on 2015/8/4.
 */
public class ReferrerInvest {

    private String investUserId;
    private int level;
    private double investMoney;
    private Date investTime;
    private double rewardMoney;
    private double rewardRate;
    private Date rewardTime;

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

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
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

    public Date getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(Date rewardTime) {
        this.rewardTime = rewardTime;
    }
}