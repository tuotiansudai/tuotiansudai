package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class CashSnowballActivityModel implements Serializable{

    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private long annualizedAmount;
    private long cashAmount;
    private Date createdTime;
    private Date updatedTime;

    public CashSnowballActivityModel() {
    }

    public CashSnowballActivityModel(String loginName, String userName, String mobile, long investAmount, long annualizedAmount, long cashAmount) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.annualizedAmount = annualizedAmount;
        this.cashAmount = cashAmount;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }
}
