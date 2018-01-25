package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class ActivityInvestAnnualizedView implements Serializable {

    private String userName;
    private String loginName;
    private String mobile;
    private long sumInvestAmount;
    private long sumAnnualizedAmount;

    public ActivityInvestAnnualizedView() {
    }

    public ActivityInvestAnnualizedView(String userName, String loginName, String mobile, Long sumInvestAmount, Long sumAnnualizedAmount) {
        this.userName = userName;
        this.loginName = loginName;
        this.mobile = mobile;
        this.sumInvestAmount = sumInvestAmount;
        this.sumAnnualizedAmount = sumAnnualizedAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
