package com.tuotiansudai.membership.repository.model;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MembershipGiveModel implements Serializable {
    private long id;
    private long membershipId;
    private int validPeriod;
    private Date receiveStartTime;
    private Date receiveEndTime;
    private String userGroup;
    private int expectAmount;
    private int actualAmount;
    private boolean valid;
    private String validLoginName;
    private Date createdTime;
    private Date updatedTime;
    private String updatedLoginName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(long membershipId) {
        this.membershipId = membershipId;
    }

    public int getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(int validPeriod) {
        this.validPeriod = validPeriod;
    }

    public Date getReceiveStartTime() {
        return receiveStartTime;
    }

    public void setReceiveStartTime(Date receiveStartTime) {
        this.receiveStartTime = receiveStartTime;
    }

    public Date getReceiveEndTime() {
        return receiveEndTime;
    }

    public void setReceiveEndTime(Date receiveEndTime) {
        this.receiveEndTime = receiveEndTime;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public List<MembershipUserGroup> getUserGroupList() {
        List<MembershipUserGroup> userGroups = new ArrayList<>();
        if (!(StringUtils.isEmpty(userGroup) || StringUtils.isEmpty(userGroup.trim()))) {
            for (String value : Splitter.on(",").omitEmptyStrings().splitToList(userGroup)) {
                userGroups.add(MembershipUserGroup.valueOf(value));
            }
        }
        return userGroups;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public void setUserGroup(List<MembershipUserGroup> membershipUserGroups) {
        this.userGroup = Joiner.on(",").join(membershipUserGroups);
    }

    public int getExpectAmount() {
        return expectAmount;
    }

    public void setExpectAmount(int expectAmount) {
        this.expectAmount = expectAmount;
    }

    public int getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(int actualAmount) {
        this.actualAmount = actualAmount;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getValidLoginName() {
        return validLoginName;
    }

    public void setValidLoginName(String validLoginName) {
        this.validLoginName = validLoginName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedLoginName() {
        return updatedLoginName;
    }

    public void setUpdatedLoginName(String updatedLoginName) {
        this.updatedLoginName = updatedLoginName;
    }
}
