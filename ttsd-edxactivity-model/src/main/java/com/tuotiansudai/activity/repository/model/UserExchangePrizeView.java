package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserExchangePrizeView implements Serializable {

    private String mobile;

    private String loginName;

    private String userName;

    private String investAmount;

    private String prize;

    private Date exchangeTime;

    public UserExchangePrizeView() {}

    public UserExchangePrizeView(UserExchangePrizeModel userExchangePrizeModel,String amount){
        this.mobile=userExchangePrizeModel.getMobile();
        this.loginName=userExchangePrizeModel.getLoginName();
        this.userName=userExchangePrizeModel.getUserName();
        this.prize=userExchangePrizeModel.getPrize().getPrizeName();
        this.investAmount=amount;
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

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    private String activityCategory;

}
