package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class IPhone7LotteryConfigModel implements Serializable {
    private long id;
    private int period;
    private String lotteryNumber;
    private Date effectiveTime;
    private String createdBy;
    private Date createdTime;
    private String auditedBy;
    private Date auditedTime;
    private IPhone7LotteryConfigStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(String auditedBy) {
        this.auditedBy = auditedBy;
    }

    public Date getAuditedTime() {
        return auditedTime;
    }

    public void setAuditedTime(Date auditedTime) {
        this.auditedTime = auditedTime;
    }

    public IPhone7LotteryConfigStatus getStatus() {
        return status;
    }

    public void setStatus(IPhone7LotteryConfigStatus status) {
        this.status = status;
    }
}
