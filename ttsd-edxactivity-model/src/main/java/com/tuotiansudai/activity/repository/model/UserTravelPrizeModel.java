package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserTravelPrizeModel implements Serializable{

    private static final long serialVersionUID = -4735243302085648734L;

    private long id;

    private long prizeId;

    private String prize;

    private String loginName;

    private String mobile;

    private String userName;

    private long investAmount;

    private Date createdTime;

    public UserTravelPrizeModel() {
    }

    public UserTravelPrizeModel(long prizeId, String prize, String loginName, String mobile, String userName, long investAmount) {
        this.prizeId = prizeId;
        this.prize = prize;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.investAmount = investAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(long prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
