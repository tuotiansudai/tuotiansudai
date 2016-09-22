package com.tuotiansudai.membership.repository.model;

import com.tuotiansudai.membership.dto.MembershipGiveDto;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class MembershipGiveModel implements Serializable {
    private long id;
    private long membershipId;
    private int deadline;
    private Date startTime;
    private Date endTime;
    private MembershipUserGroup userGroup;
    private boolean smsNotify;
    private boolean active;
    private String activeBy;
    private Date createdTime;
    private String createdBy;
    private Date updatedTime;
    private String updatedBy;

    public MembershipGiveModel() {
    }

    public MembershipGiveModel(MembershipGiveDto membershipGiveDto, MembershipModel membershipModel) {
        this.membershipId = membershipModel.getId();
        this.deadline = membershipGiveDto.getDeadline();
        if (!StringUtils.isEmpty(membershipGiveDto.getStartTime())) {
            this.startTime = DateTime.parse(membershipGiveDto.getStartTime()).toDate();
        }
        if (!StringUtils.isEmpty(membershipGiveDto.getEndTime())) {
            this.endTime = DateTime.parse(membershipGiveDto.getEndTime()).toDate();
        }
        this.userGroup = membershipGiveDto.getUserGroup();
        this.smsNotify = membershipGiveDto.isSmsNotify();
        this.active = membershipGiveDto.isActive();
        this.activeBy = membershipGiveDto.getActiveBy();
        this.createdTime = new Date();
        this.createdBy = membershipGiveDto.getCreatedBy();
        this.updatedTime = new Date();
        this.updatedBy = membershipGiveDto.getUpdatedBy();
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

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
