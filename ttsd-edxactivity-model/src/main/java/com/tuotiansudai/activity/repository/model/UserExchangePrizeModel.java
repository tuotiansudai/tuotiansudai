package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserExchangePrizeModel implements Serializable {

    private long id;

    private String mobile;

    private String loginName;

    private String userName;

    private ExchangePrize prize;

    private Date exchangeTime;

    private ActivityCategory activityCategory;

    public UserExchangePrizeModel() {}

    public UserExchangePrizeModel(String mobile, String loginName, String userName, ExchangePrize prize, Date exchangeTime, ActivityCategory activityCategory) {
        this.mobile = mobile;
        this.loginName = loginName;
        this.userName = userName;
        this.prize = prize;
        this.exchangeTime = exchangeTime;
        this.activityCategory = activityCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public ExchangePrize getPrize() {
        return prize;
    }

    public void setPrize(ExchangePrize prize) {
        this.prize = prize;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public ActivityCategory getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(ActivityCategory activityCategory) {
        this.activityCategory = activityCategory;
    }
}
