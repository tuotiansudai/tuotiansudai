package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryPrizeModel implements Serializable {

    private long id;

    private String mobile;

    private String loginName;

    private String userName;

    private LotteryPrize prize;

    private Date lotteryTime;

    private ActivityCategory activityCategory;

    public UserLotteryPrizeModel() {}

    public UserLotteryPrizeModel(String mobile, String loginName, String userName, LotteryPrize prize, Date lotteryTime, ActivityCategory prizeType) {
        this.mobile = mobile;
        this.loginName = loginName;
        this.userName = userName;
        this.prize = prize;
        this.lotteryTime = lotteryTime;
        this.activityCategory = prizeType;
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

    public LotteryPrize getPrize() {
        return prize;
    }

    public void setPrize(LotteryPrize prize) {
        this.prize = prize;
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ActivityCategory getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(ActivityCategory activityCategory) {
        this.activityCategory = activityCategory;
    }
}
