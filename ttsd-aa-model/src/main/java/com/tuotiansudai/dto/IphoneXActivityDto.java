package com.tuotiansudai.dto;


import java.io.Serializable;

public class IphoneXActivityDto implements Serializable{

    private String loginName;
    private String mobile;
    private String sumInvestAmount;
    private String sumAnnualizedAmount;

    public IphoneXActivityDto() {
    }

    public IphoneXActivityDto(String loginName, String mobile, String sumInvestAmount, String sumAnnualizedAmount) {
        this.loginName = loginName;
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
}
