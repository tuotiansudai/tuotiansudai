package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestReferrerRewardModel implements Serializable {

    private long id;

    private long investId;

    private long amount;

    private String referrerLoginName;

    private Role referrerRole;

    private ReferrerRewardStatus status;

    private Date createdTime = new Date();

    public InvestReferrerRewardModel() {
    }

    public InvestReferrerRewardModel(long id, long investId, long amount, String referrerLoginName, Role referrerRole) {
        this.id = id;
        this.investId = investId;
        this.amount = amount;
        this.referrerLoginName = referrerLoginName;
        this.referrerRole = referrerRole;
        this.status = ReferrerRewardStatus.FAILURE;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public Role getReferrerRole() {
        return referrerRole;
    }

    public void setReferrerRole(Role referrerRole) {
        this.referrerRole = referrerRole;
    }

    public ReferrerRewardStatus getStatus() {
        return status;
    }

    public void setStatus(ReferrerRewardStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
