package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinSignPropertyModel implements Serializable {

    private long id;

    private String loginName;

    private String anxinUserId;

    private boolean skipAuth = false;

    private String projectCode;

    private String authIp;

    private Date authTime;

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

    public String getAuthIp() {
        return authIp;
    }

    public void setAuthIp(String authIp) {
        this.authIp = authIp;
    }

    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isAnxinUser() {
        return this.anxinUserId != null && this.anxinUserId.length() > 0;
    }
}
