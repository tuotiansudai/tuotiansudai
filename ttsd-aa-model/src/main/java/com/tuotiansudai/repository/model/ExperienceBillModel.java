package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBusinessType;

import java.io.Serializable;
import java.util.Date;

public class ExperienceBillModel implements Serializable {

    private long id;
    private String loginName;
    private ExperienceBillOperationType operationType;
    private long amount;
    private ExperienceBusinessType businessType;
    private String note;
    private Date createdTime;

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

    public ExperienceBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ExperienceBillOperationType operationType) {
        this.operationType = operationType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public ExperienceBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(ExperienceBusinessType businessType) {
        this.businessType = businessType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
