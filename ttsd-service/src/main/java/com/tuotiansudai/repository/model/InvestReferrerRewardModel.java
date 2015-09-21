package com.tuotiansudai.repository.model;

import java.util.Date;

public class InvestReferrerRewardModel {
    private long id ;
    private long investId;
    private Date time;
    private long bonus;
    private String referrerLoginName;
    private ReferrerRewardStatus status;
    private Role roleName;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public ReferrerRewardStatus getStatus() {
        return status;
    }

    public void setStatus(ReferrerRewardStatus status) {
        this.status = status;
    }

    public Role getRoleName() {
        return roleName;
    }

    public void setRoleName(Role roleName) {
        this.roleName = roleName;
    }

}
