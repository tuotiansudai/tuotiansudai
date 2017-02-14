package com.tuotiansudai.console.repository.model;

import java.io.Serializable;
import java.util.Date;

public class RemainUserView implements Serializable {
    private String loginName;
    private String mobile;
    private String userName;
    private Date registerTime;
    private boolean useExperienceCoupon;
    private Date experienceTime;
    private int investCount;
    private long investSum;
    private Date firstInvestTime;
    private long firstInvestAmount;
    private Date secondInvestTime;
    private long secondInvestAmount;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public boolean isUseExperienceCoupon() {
        return useExperienceCoupon;
    }

    public void setUseExperienceCoupon(boolean useExperienceCoupon) {
        this.useExperienceCoupon = useExperienceCoupon;
    }

    public Date getExperienceTime() {
        return experienceTime;
    }

    public void setExperienceTime(Date experienceTime) {
        this.experienceTime = experienceTime;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public long getInvestSum() {
        return investSum;
    }

    public void setInvestSum(long investSum) {
        this.investSum = investSum;
    }

    public Date getFirstInvestTime() {
        return firstInvestTime;
    }

    public void setFirstInvestTime(Date firstInvestTime) {
        this.firstInvestTime = firstInvestTime;
    }

    public long getFirstInvestAmount() {
        return firstInvestAmount;
    }

    public void setFirstInvestAmount(long firstInvestAmount) {
        this.firstInvestAmount = firstInvestAmount;
    }

    public Date getSecondInvestTime() {
        return secondInvestTime;
    }

    public void setSecondInvestTime(Date secondInvestTime) {
        this.secondInvestTime = secondInvestTime;
    }

    public long getSecondInvestAmount() {
        return secondInvestAmount;
    }

    public void setSecondInvestAmount(long secondInvestAmount) {
        this.secondInvestAmount = secondInvestAmount;
    }
}
