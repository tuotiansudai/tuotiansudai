package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class DragonBoatFestivalModel implements Serializable {
    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private long investAmount;
    private long experienceAmount;
    private long inviteNewUserCount;
    private long inviteOldUserCount;
    private Date createdTime;

    public DragonBoatFestivalModel(){}

    public DragonBoatFestivalModel(String loginName,
                                   String userName,
                                   String mobile,
                                   long investAmount,
                                   long experienceAmount,
                                   long inviteNewUserCount,
                                   long inviteOldUserCount){
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investAmount = investAmount;
        this.experienceAmount = experienceAmount;
        this.inviteNewUserCount = inviteNewUserCount;
        this.inviteOldUserCount = inviteOldUserCount;
        this.createdTime = new Date();
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

    public long getExperienceAmount() {
        return experienceAmount;
    }

    public void setExperienceAmount(long experienceAmount) {
        this.experienceAmount = experienceAmount;
    }

    public long getInviteNewUserCount() {
        return inviteNewUserCount;
    }

    public void setInviteNewUserCount(long inviteNewUserCount) {
        this.inviteNewUserCount = inviteNewUserCount;
    }

    public long getInviteOldUserCount() {
        return inviteOldUserCount;
    }

    public void setInviteOldUserCount(long inviteOldUserCount) {
        this.inviteOldUserCount = inviteOldUserCount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
