package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class DragonBoatFestivalView implements Serializable {

    private String userName;

    private String mobile;

    private long totalInvestAmount;

    private String pkGroup;

    private long pkInvestAmount;

    private long inviteExperienceAmount;

    private long pkExperienceAmount;

    private long inviteOldUserCount;

    private long inviteNewUserCount;

    public DragonBoatFestivalView(String userName, String mobile, long totalInvestAmount, String pkGroup, long pkInvestAmount,
                                  long inviteExperienceAmount, long pkExperienceAmount, long inviteOldUserCount, long inviteNewUserCount) {
        this.userName = userName;
        this.mobile = mobile;
        this.totalInvestAmount = totalInvestAmount;
        this.pkGroup = pkGroup;
        this.pkInvestAmount = pkInvestAmount;
        this.inviteExperienceAmount = inviteExperienceAmount;
        this.pkExperienceAmount = pkExperienceAmount;
        this.inviteOldUserCount = inviteOldUserCount;
        this.inviteNewUserCount = inviteNewUserCount;
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

    public String getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(String pkGroup) {
        this.pkGroup = pkGroup;
    }

    public long getPkInvestAmount() {
        return pkInvestAmount;
    }

    public void setPkInvestAmount(long pkInvestAmount) {
        this.pkInvestAmount = pkInvestAmount;
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

    public long getInviteOldUserCount() {
        return inviteOldUserCount;
    }

    public void setInviteOldUserCount(long inviteOldUserCount) {
        this.inviteOldUserCount = inviteOldUserCount;
    }

    public long getInviteNewUserCount() {
        return inviteNewUserCount;
    }

    public void setInviteNewUserCount(long inviteNewUserCount) {
        this.inviteNewUserCount = inviteNewUserCount;
    }
}
