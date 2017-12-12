package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;

public class CashSnowballActivityView implements Serializable{

    private String userName;
    private String mobile;
    private long investAmount;
    private long annualizedAmount;
    private long cashAmount;

    public CashSnowballActivityView() {
    }

    public CashSnowballActivityView(CashSnowballActivityModel cashSnowballActivityModel) {
        this.userName = cashSnowballActivityModel.getUserName();
        this.mobile = cashSnowballActivityModel.getMobile();
        this.investAmount = cashSnowballActivityModel.getInvestAmount();
        this.annualizedAmount = cashSnowballActivityModel.getAnnualizedAmount();
        this.cashAmount = cashSnowballActivityModel.getCashAmount();
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getAnnualizedAmount() {
        return annualizedAmount;
    }

    public void setAnnualizedAmount(long annualizedAmount) {
        this.annualizedAmount = annualizedAmount;
    }

    public long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(long cashAmount) {
        this.cashAmount = cashAmount;
    }
}
