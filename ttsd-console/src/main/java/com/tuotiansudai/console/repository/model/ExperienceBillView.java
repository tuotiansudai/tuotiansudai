package com.tuotiansudai.console.repository.model;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBillBusinessType;

import java.io.Serializable;
import java.util.Date;

public class ExperienceBillView implements Serializable{
    private long id;
    private Date createdTime;
    private String loginName;
    private String userName;
    private String mobile;
    private ExperienceBillOperationType operationType;
    private ExperienceBillBusinessType businessType;
    private long amount;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ExperienceBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(ExperienceBillOperationType operationType) {
        this.operationType = operationType;
    }

    public ExperienceBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(ExperienceBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
