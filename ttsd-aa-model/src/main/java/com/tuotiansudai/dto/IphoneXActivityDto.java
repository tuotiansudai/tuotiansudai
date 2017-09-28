package com.tuotiansudai.dto;

import java.io.Serializable;

public class IphoneXActivityDto implements Serializable{

    private String loginName;
    private String userName;
    private String mobile;
    private long sumInvestAmount;
    private long sumAnnualizedAmount;

    public IphoneXActivityDto() {
    }

    public IphoneXActivityDto(String loginName, String userName, String mobile, long sumInvestAmount, long sumAnnualizedAmount) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.sumInvestAmount = sumInvestAmount;
        this.sumAnnualizedAmount = sumAnnualizedAmount;
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

    public long getSumInvestAmount() {
        return sumInvestAmount;
    }

    public void setSumInvestAmount(long sumInvestAmount) {
        this.sumInvestAmount = sumInvestAmount;
    }

    public long getSumAnnualizedAmount() {
        return sumAnnualizedAmount;
    }

    public void setSumAnnualizedAmount(long sumAnnualizedAmount) {
        this.sumAnnualizedAmount = sumAnnualizedAmount;
    }
}
