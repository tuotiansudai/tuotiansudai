package com.tuotiansudai.membership.dto;

import com.tuotiansudai.membership.repository.model.MembershipGiveModel;
import com.tuotiansudai.membership.repository.model.MembershipUserGroup;

import java.text.SimpleDateFormat;

public class MembershipGiveDto {
    private long id;
    private int membershipLevel;
    private int deadline;
    private String startTime;
    private String endTime;
    private MembershipUserGroup userGroup;
    private boolean smsNotify;
    private boolean active;
    private String activeBy;
    private String createdBy;
    private String updatedBy;

    public MembershipGiveDto() {
    }

    public MembershipGiveDto(MembershipGiveModel membershipGiveModel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.id = membershipGiveModel.getId();
//        private int membershipLevel;
        this.deadline = membershipGiveModel.getDeadline();
        if (null != membershipGiveModel.getStartTime()) {
            this.startTime = simpleDateFormat.format(membershipGiveModel.getStartTime());
        }
        if (null != membershipGiveModel.getEndTime()) {
            this.endTime = simpleDateFormat.format(membershipGiveModel.getEndTime());
        }
        this.userGroup = membershipGiveModel.getUserGroup();
        this.smsNotify = membershipGiveModel.isSmsNotify();
        this.active = membershipGiveModel.isActive();
        this.activeBy = membershipGiveModel.getActiveBy();
        this.createdBy = membershipGiveModel.getCreatedBy();
        this.updatedBy = membershipGiveModel.getUpdatedBy();
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

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActiveBy() {
        return activeBy;
    }

    public void setActiveBy(String activeBy) {
        this.activeBy = activeBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
