package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class CashSnowballActivityModel implements Serializable{

    private long id;
    private long investId;
    private String loginName;
    private String userName;
    private String mobile;
    private long annualizedAmount;
    private long cashAmount;
    private Date createdTime;
    private Date updatedTime;

    public CashSnowballActivityModel() {
    }

    public CashSnowballActivityModel(long investId, String loginName, String userName, String mobile, long annualizedAmount, long cashAmount) {
        this.investId = investId;
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.annualizedAmount = annualizedAmount;
        this.cashAmount = cashAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
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
}
