package com.tuotiansudai.repository.model;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class HeroRankingView implements Serializable {
    private String loginName;
    private long sumAmount;
    private String userName;
    private String mobile;
    private String centSumAmount;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
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

    public String getCentSumAmount() {
        return AmountConverter.convertCentToString(this.sumAmount);
    }
}
