package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinSignPropertyModel implements Serializable {

    private long id;

    private String loginName;

    private String anxinUserId;

    private boolean skipAuth;

    private String projectCode;

    private String ip;

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

    public String getAnxinUserId() {
        return anxinUserId;
    }

    public void setAnxinUserId(String anxinUserId) {
        this.anxinUserId = anxinUserId;
    }

    public boolean isSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(boolean skipAuth) {
        this.skipAuth = skipAuth;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
