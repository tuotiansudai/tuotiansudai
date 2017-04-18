package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class HTrackingUserModel implements Serializable{

    private long id;
    private String mobile;
    private String deviceId;
    private Date createdTime;

    public HTrackingUserModel(){}

    public HTrackingUserModel(String mobile, String deviceId) {
        this.mobile = mobile;
        this.deviceId = deviceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
