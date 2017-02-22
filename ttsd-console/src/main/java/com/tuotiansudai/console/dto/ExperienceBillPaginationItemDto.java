package com.tuotiansudai.console.dto;


import com.tuotiansudai.console.repository.model.ExperienceBillView;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBusinessType;

import java.io.Serializable;
import java.util.Date;

public class ExperienceBillPaginationItemDto implements Serializable {

    private Date createdTime;
    private String loginName;
    private String userName;
    private String mobile;
    private ExperienceBillOperationType operationType;
    private ExperienceBusinessType businessType;
    private long amount;

    public ExperienceBillPaginationItemDto() {
    }

    public ExperienceBillPaginationItemDto(ExperienceBillView experienceBillView) {
        this.createdTime = experienceBillView.getCreatedTime();
        this.loginName = experienceBillView.getLoginName();
        this.userName = experienceBillView.getUserName();
        this.mobile = experienceBillView.getMobile();
        this.operationType = experienceBillView.getOperationType();
        this.businessType = experienceBillView.getBusinessType();
        this.amount = experienceBillView.getAmount();
    }

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

    public ExperienceBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(ExperienceBusinessType businessType) {
        this.businessType = businessType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
