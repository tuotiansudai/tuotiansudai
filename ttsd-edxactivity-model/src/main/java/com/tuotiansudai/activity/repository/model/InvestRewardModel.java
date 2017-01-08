package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class InvestRewardModel implements Serializable {
    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private long rewardGrade;
    private Date updatedTime;
    private Date createdTime;

    public InvestRewardModel(){}

    public InvestRewardModel(String loginName, String userName, String mobile, long investAmount, Long rewardGrade) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.rewardGrade = rewardGrade;
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getRewardGrade() {
        return rewardGrade;
    }

    public void setRewardGrade(long rewardGrade) {
        this.rewardGrade = rewardGrade;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
