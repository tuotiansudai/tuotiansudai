package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class WeChatUserModel implements Serializable {
    private long id;

    private String loginName;

    private String openid;

    private boolean bound;

    private Date latestLoginTime;

    private Date createdTime;

    public WeChatUserModel() {
    }

    public WeChatUserModel(String loginName, String openid) {
        this.loginName = loginName;
        this.openid = openid;
        this.bound = false;
        this.latestLoginTime = new Date();
        this.createdTime = this.latestLoginTime;
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public boolean isBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public Date getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
