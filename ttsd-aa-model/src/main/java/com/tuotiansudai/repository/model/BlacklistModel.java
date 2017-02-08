package com.tuotiansudai.repository.model;

import java.util.Date;

public class BlacklistModel {
    private long id;
    private String loginName;
    private Date createdTime;
    private Date updatedTime;
    private boolean deleted;

    public BlacklistModel() {
    }

    public BlacklistModel(String loginName, Date createdTime, boolean deleted) {
        this.loginName = loginName;
        this.createdTime = createdTime;
        this.updatedTime = createdTime;
        this.deleted = deleted;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
