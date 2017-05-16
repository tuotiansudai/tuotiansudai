package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class MidSummerSharedUsersModel implements Serializable {

    private long id;

    private String loginName;

    private Date createdTime;

    public MidSummerSharedUsersModel() {
    }

    public MidSummerSharedUsersModel(String loginName) {
        this.loginName = loginName;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
