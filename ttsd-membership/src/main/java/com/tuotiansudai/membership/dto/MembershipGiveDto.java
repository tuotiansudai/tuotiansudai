package com.tuotiansudai.membership.dto;

import com.tuotiansudai.membership.repository.model.MembershipGiveModel;
import com.tuotiansudai.membership.repository.model.MembershipUserGroup;

import java.text.SimpleDateFormat;

public class MembershipGiveDto {
    private long id;
    private int membershipLevel;
    private int validPeriod;
    private String receiveStartTime;
    private String receiveEndTime;
    private MembershipUserGroup userGroup;
    private boolean smsNotify;
    private boolean valid;
    private String validLoginName;
    private String createdLoginName;
    private String updatedLoginName;

    public MembershipGiveDto() {
    }

    public MembershipGiveDto(MembershipGiveModel membershipGiveModel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.id = membershipGiveModel.getId();
//        private int membershipLevel;
        this.validPeriod = membershipGiveModel.getValidPeriod();
        if (null != membershipGiveModel.getReceiveStartTime()) {
            this.receiveStartTime = simpleDateFormat.format(membershipGiveModel.getReceiveStartTime());
        }
        if (null != membershipGiveModel.getReceiveEndTime()) {
            this.receiveEndTime = simpleDateFormat.format(membershipGiveModel.getReceiveEndTime());
        }
        this.userGroup = membershipGiveModel.getUserGroup();
        this.smsNotify = membershipGiveModel.isSmsNotify();
        this.valid = membershipGiveModel.isValid();
        this.validLoginName = membershipGiveModel.getValidLoginName();
        this.createdLoginName = membershipGiveModel.getCreatedLoginName();
        this.updatedLoginName = membershipGiveModel.getUpdatedLoginName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public int getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(int validPeriod) {
        this.validPeriod = validPeriod;
    }

    public String getReceiveStartTime() {
        return receiveStartTime;
    }

    public void setReceiveStartTime(String receiveStartTime) {
        this.receiveStartTime = receiveStartTime;
    }

    public String getReceiveEndTime() {
        return receiveEndTime;
    }

    public void setReceiveEndTime(String receiveEndTime) {
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

    public String getCreatedLoginName() {
        return createdLoginName;
    }

    public void setCreatedLoginName(String createdLoginName) {
        this.createdLoginName = createdLoginName;
    }

    public String getUpdatedLoginName() {
        return updatedLoginName;
    }

    public void setUpdatedLoginName(String updatedLoginName) {
        this.updatedLoginName = updatedLoginName;
    }
}
