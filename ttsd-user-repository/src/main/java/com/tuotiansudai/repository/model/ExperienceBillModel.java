package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBillBusinessType;

import java.io.Serializable;
import java.util.Date;

public class ExperienceBillModel implements Serializable {

    private long id;
    private String loginName;
    private ExperienceBillOperationType operationType;
    private long amount;
    private ExperienceBillBusinessType businessType;
    private String note;
    private Date createdTime;


    public ExperienceBillModel(){

    }

    public ExperienceBillModel(String loginName, ExperienceBillOperationType experienceBillOperationType, long amount, ExperienceBillBusinessType experienceBusinessType, String note){
        this.loginName = loginName;
        this.operationType = experienceBillOperationType;
        this.amount = amount;
        this.businessType = experienceBusinessType;
        this.note = note;
        this.createdTime = new Date();
    }

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

    public ExperienceBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(ExperienceBillBusinessType businessType) {
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
