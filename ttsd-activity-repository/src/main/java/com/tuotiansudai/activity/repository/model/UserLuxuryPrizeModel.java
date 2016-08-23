package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserLuxuryPrizeModel implements Serializable{

    private static final long serialVersionUID = 7365763763324921833L;

    private long id;

    private long prizeId;

    private long prize;

    private String loginName;

    private String mobile;

    private long investAmount;

    private Date createdTime;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getPrize() {
        return prize;
    }

    public void setPrize(long prize) {
        this.prize = prize;
    }
}
