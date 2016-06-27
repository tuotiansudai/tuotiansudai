package com.tuotiansudai.membership.dto;

import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipType;

import java.util.Date;

public class UserMembershipItemDto {
    private String loginName;
    private String realName;
    private String mobile;
    private long membershipPoint;
    private int membershipLevel;
    private UserMembershipType userMembershipType;
    private Date registerTime;

    public UserMembershipItemDto() {
    }

    public UserMembershipItemDto(UserMembershipItemView userMembershipItemView) {
        this.loginName = userMembershipItemView.getLoginName();
        this.realName = userMembershipItemView.getRealName();
        this.mobile = userMembershipItemView.getMobile();
        this.membershipPoint = userMembershipItemView.getMembershipPoint();
        this.membershipLevel = userMembershipItemView.getMembershipLevel();
        this.userMembershipType = userMembershipItemView.getUserMembershipType();
        this.registerTime = userMembershipItemView.getRegisterTime();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getMembershipPoint() {
        return membershipPoint;
    }

    public void setMembershipPoint(long membershipPoint) {
        this.membershipPoint = membershipPoint;
    }

    public int getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public UserMembershipType getUserMembershipType() {
        return userMembershipType;
    }

    public void setUserMembershipType(UserMembershipType userMembershipType) {
        this.userMembershipType = userMembershipType;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
