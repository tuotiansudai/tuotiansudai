package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class DragonBoatFestivalView implements Serializable {

    private String userName;

    private String mobile;

    private String totalInvestAmount;

    private String pkGroup;

    private String pkInvestAmount;

    private String inviteExperienceAmount;

    private String pkExperienceAmount;

    private long inviteOldUserCount;

    private long inviteNewUserCount;

    public DragonBoatFestivalView(String userName, String mobile, long totalInvestAmount, String pkGroup, long pkInvestAmount,
                                  long inviteExperienceAmount, long pkExperienceAmount, long inviteOldUserCount, long inviteNewUserCount) {
        this.userName = userName;
        this.mobile = mobile;
        this.totalInvestAmount = AmountConverter.convertCentToString(totalInvestAmount);
        this.pkGroup = pkGroup;
        this.pkInvestAmount = AmountConverter.convertCentToString(pkInvestAmount);
        this.inviteExperienceAmount = AmountConverter.convertCentToString(inviteExperienceAmount);
        this.pkExperienceAmount = AmountConverter.convertCentToString(pkExperienceAmount);
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

    public String getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(String totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

    public String getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(String pkGroup) {
        this.pkGroup = pkGroup;
    }

    public String getPkInvestAmount() {
        return pkInvestAmount;
    }

    public void setPkInvestAmount(String pkInvestAmount) {
        this.pkInvestAmount = pkInvestAmount;
    }

    public String getInviteExperienceAmount() {
        return inviteExperienceAmount;
    }

    public void setInviteExperienceAmount(String inviteExperienceAmount) {
        this.inviteExperienceAmount = inviteExperienceAmount;
    }

    public String getPkExperienceAmount() {
        return pkExperienceAmount;
    }

    public void setPkExperienceAmount(String pkExperienceAmount) {
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
