package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class ActivityInvestModel implements Serializable{

    private long id;
    private long loanId;
    private long investId;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private long annualizedAmount;
    private String activityName;
    private Date createdTime;

    public ActivityInvestModel() {
    }

    public ActivityInvestModel(long loanId, long investId, String loginName, String userName, String mobile, long investAmount, long annualizedAmount, String activityName) {
        this.loanId = loanId;
        this.investId = investId;
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.annualizedAmount = annualizedAmount;
        this.activityName = activityName;
        this.createdTime = new Date();
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getAnnualizedAmount() {
        return annualizedAmount;
    }

    public void setAnnualizedAmount(long annualizedAmount) {
        this.annualizedAmount = annualizedAmount;
    }
}
