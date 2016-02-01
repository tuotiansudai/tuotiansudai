package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class AutoInvestPlanModel implements Serializable {
    private long id;
    private String loginName;
    private long minInvestAmount;
    private long maxInvestAmount;
    private long retentionAmount;
    private int autoInvestPeriods;
    private boolean enabled;
    private Date createdTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(long maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public long getRetentionAmount() {
        return retentionAmount;
    }

    public void setRetentionAmount(long retentionAmount) {
        this.retentionAmount = retentionAmount;
    }

    public int getAutoInvestPeriods() {
        return autoInvestPeriods;
    }

    public void setAutoInvestPeriods(int autoInvestPeriods) {
        this.autoInvestPeriods = autoInvestPeriods;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
