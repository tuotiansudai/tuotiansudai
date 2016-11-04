package com.tuotiansudai.cfca.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinSignRequestModel implements Serializable{
    private long id;
    private String userId;
    private String authorizationTime;
    private String location;
    private String signLocation;
    private String projectCode;
    private String isProxySign;
    private String isCopy;
    private Date createdTime;

    public AnxinSignRequestModel() {
    }

    public AnxinSignRequestModel(String userId, String authorizationTime, String location, String signLocation, String projectCode, String isProxySign, String isCopy, Date createdTime) {
        this.userId = userId;
        this.authorizationTime = authorizationTime;
        this.location = location;
        this.signLocation = signLocation;
        this.projectCode = projectCode;
        this.isProxySign = isProxySign;
        this.isCopy = isCopy;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorizationTime() {
        return authorizationTime;
    }

    public void setAuthorizationTime(String authorizationTime) {
        this.authorizationTime = authorizationTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(String signLocation) {
        this.signLocation = signLocation;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getIsProxySign() {
        return isProxySign;
    }

    public void setIsProxySign(String isProxySign) {
        this.isProxySign = isProxySign;
    }

    public String isCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
