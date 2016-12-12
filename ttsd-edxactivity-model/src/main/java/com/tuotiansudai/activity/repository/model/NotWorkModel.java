package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class NotWorkModel implements Serializable {
    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private long recommendedInvestAmount;
    private boolean sendCoupon;

    public NotWorkModel() {
    }

    public NotWorkModel(String loginName, String userName, String mobile, boolean sendCoupon) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.sendCoupon = sendCoupon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getRecommendedInvestAmount() {
        return recommendedInvestAmount;
    }

    public void setRecommendedInvestAmount(long recommendedInvestAmount) {
        this.recommendedInvestAmount = recommendedInvestAmount;
    }

    public boolean isSendCoupon() {
        return sendCoupon;
    }

    public void setSendCoupon(boolean sendCoupon) {
        this.sendCoupon = sendCoupon;
    }
}