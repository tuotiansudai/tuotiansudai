package com.esoft.archer.user.model;

/**
 * Created by Administrator on 2015/8/4.
 */
public class ReferrerInvest {

    private String investUserId;
    private int level;
    private String loanId;
    private String loanName;
    private double investMoney;
    private String investTime;
    private double rewardMoney;
    private String rewardTime;
    private String loanActivityType;
    private int deadLine;

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

    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }

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

    public String getLoanActivityType() {
        return loanActivityType;
    }

    public void setLoanActivityType(String loanActivityType) {
        this.loanActivityType = loanActivityType;
    }

    public int getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(int deadLine) {
        this.deadLine = deadLine;
    }
}