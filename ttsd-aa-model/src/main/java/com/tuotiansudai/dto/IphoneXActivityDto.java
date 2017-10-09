package com.tuotiansudai.dto;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class IphoneXActivityDto implements Serializable{

    private String loginName;
    private String userName;
    private String mobile;
    private String sumInvestAmount;
    private String sumAnnualizedAmount;
    private String reward;

    public IphoneXActivityDto() {
    }

    public IphoneXActivityDto(String loginName, String userName, String mobile, long sumInvestAmount, long sumAnnualizedAmount, String reward) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.sumInvestAmount = AmountConverter.convertCentToString(sumInvestAmount);
        this.sumAnnualizedAmount = AmountConverter.convertCentToString(sumAnnualizedAmount);
        this.reward = reward;
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

    public String getSumInvestAmount() {
        return sumInvestAmount;
    }

    public void setSumInvestAmount(String sumInvestAmount) {
        this.sumInvestAmount = sumInvestAmount;
    }

    public String getSumAnnualizedAmount() {
        return sumAnnualizedAmount;
    }

    public void setSumAnnualizedAmount(String sumAnnualizedAmount) {
        this.sumAnnualizedAmount = sumAnnualizedAmount;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
