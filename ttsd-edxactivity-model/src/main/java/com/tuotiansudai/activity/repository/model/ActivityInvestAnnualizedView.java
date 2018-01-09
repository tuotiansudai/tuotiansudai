package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class ActivityInvestAnnualizedView implements Serializable {

    private String userName;
    private String loginName;
    private String mobile;
    private long investAmount;
    private long annualizedAmount;

    public ActivityInvestAnnualizedView() {
    }

    public ActivityInvestAnnualizedView(String userName, String loginName, String mobile, Long investAmount, Long annualizedAmount) {
        this.userName = userName;
        this.loginName = loginName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.annualizedAmount = annualizedAmount;
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getAnnualizedAmount() {
        return annualizedAmount;
    }

    public void setAnnualizedAmount(long annualizedAmount) {
        this.annualizedAmount = annualizedAmount;
    }
}
