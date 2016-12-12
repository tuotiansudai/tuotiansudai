package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class AnnualPrizeModel implements Serializable {
    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private boolean firstSendCoupon;
    private boolean secondSendCoupon;
    private Date updatedTime;
    private Date createdTime;

    public AnnualPrizeModel(String loginName,String userName, String mobile, long investAmount, boolean firstSendCoupon, boolean secondSendCoupon) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.firstSendCoupon = firstSendCoupon;
        this.secondSendCoupon = secondSendCoupon;
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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public boolean isFirstSendCoupon() {
        return firstSendCoupon;
    }

    public void setFirstSendCoupon(boolean firstSendCoupon) {
        this.firstSendCoupon = firstSendCoupon;
    }

    public boolean isSecondSendCoupon() {
        return secondSendCoupon;
    }

    public void setSecondSendCoupon(boolean secondSendCoupon) {
        this.secondSendCoupon = secondSendCoupon;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
