package com.tuotiansudai.membership.repository.model;

import com.tuotiansudai.membership.dto.MembershipGiveDto;

import java.io.Serializable;
import java.util.Date;

public class MembershipGiveModel implements Serializable {
    private long id;
    private long membershipId;
    private int validPeriod;
    private Date receiveStartTime;
    private Date receiveEndTime;
    private MembershipUserGroup userGroup;
    private boolean smsNotify;
    private boolean valid;
    private String validLoginName;
    private Date createdTime;
    private String createdLoginName;
    private Date updatedTime;
    private String updatedLoginName;

    public MembershipGiveModel() {
    }

    public MembershipGiveModel(MembershipGiveDto membershipGiveDto, MembershipModel membershipModel) {
        this.membershipId = membershipModel.getId();
        this.validPeriod = membershipGiveDto.getValidPeriod();
        this.receiveStartTime = membershipGiveDto.getReceiveStartTime();
        this.receiveEndTime = membershipGiveDto.getReceiveEndTime();
        this.userGroup = membershipGiveDto.getUserGroup();
        this.smsNotify = membershipGiveDto.isSmsNotify();
        this.valid = membershipGiveDto.isValid();
        this.validLoginName = membershipGiveDto.getValidLoginName();
        this.createdTime = new Date();
        this.createdLoginName = membershipGiveDto.getCreatedLoginName();
        this.updatedTime = new Date();
        this.updatedLoginName = membershipGiveDto.getCreatedLoginName();
    }

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

    public MembershipUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(MembershipUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public boolean isSmsNotify() {
        return smsNotify;
    }

    public void setSmsNotify(boolean smsNotify) {
        this.smsNotify = smsNotify;
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

    public String getCreatedLoginName() {
        return createdLoginName;
    }

    public void setCreatedLoginName(String createdLoginName) {
        this.createdLoginName = createdLoginName;
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
