package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;


public class DragonBoatFestivalModel implements Serializable {
    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private long totalInvestAmount;
    private long pkInvestAmount;
    private String pkGroup;
    private long inviteExperienceAmount;
    private long pkExperienceAmount;
    private long inviteNewUserCount;
    private long inviteOldUserCount;
    private Date createdTime;

    public DragonBoatFestivalModel() {
    }

    public DragonBoatFestivalModel(String loginName, String userName, String mobile) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
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

    public long getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(long totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

    public long getPkInvestAmount() {
        return pkInvestAmount;
    }

    public void setPkInvestAmount(long pkInvestAmount) {
        this.pkInvestAmount = pkInvestAmount;
    }

    public String getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(String pkGroup) {
        this.pkGroup = pkGroup;
    }

    public long getInviteExperienceAmount() {
        return inviteExperienceAmount;
    }

    public void setInviteExperienceAmount(long inviteExperienceAmount) {
        this.inviteExperienceAmount = inviteExperienceAmount;
    }

    public long getPkExperienceAmount() {
        return pkExperienceAmount;
    }

    public void setPkExperienceAmount(long pkExperienceAmount) {
        this.pkExperienceAmount = pkExperienceAmount;
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
