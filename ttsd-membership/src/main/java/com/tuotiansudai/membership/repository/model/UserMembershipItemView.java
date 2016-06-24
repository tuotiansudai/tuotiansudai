package com.tuotiansudai.membership.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserMembershipItemView implements Serializable{
    private long id;
    private String loginName;
    private String realName;
    private String mobile;
    private long membershipPoint;
    private int membershipLevel;
    private UserMembershipType userMembershipType;
    private Date registerTime;
    private Date createdTime;

    public UserMembershipItemView() {
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

    public String getMemebershipLevelString() {
        return "V" + this.getMembershipLevel();
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
