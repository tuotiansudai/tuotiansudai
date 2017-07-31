package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class ActivityInvestRewardView implements Serializable {
    private String loginName;
    private String userName;
    private String mobile;
    private String sumAmount;
    private String reward;
    private String experience;

    public ActivityInvestRewardView() {
    }

    public ActivityInvestRewardView(ActivityInvestView activityInvestView , String reward, long experience) {
        this.loginName = activityInvestView.getLoginName();
        this.userName = activityInvestView.getUserName();
        this.mobile = activityInvestView.getMobile();
        this.sumAmount = AmountConverter.convertCentToString(activityInvestView.getSumAmount());
        this.reward = reward;
        this.experience = AmountConverter.convertCentToString(experience);
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
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

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
