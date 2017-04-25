package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class MothersDayView implements Serializable{

    private String loginName;
    private String name;
    private String mobile;
    private String investAmount;
    private String reward;

    public MothersDayView() {
    }

    public MothersDayView(String loginName, String name, String mobile, String investAmount, String reward) {
        this.loginName = loginName;
        this.name = name;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.reward = reward;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
