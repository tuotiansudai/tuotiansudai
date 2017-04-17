package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class HTrackingUserModel implements Serializable{

    private String mobile;
    private String deviceId;
    private Date updatedTime;
    private Date createdTime;

    public HTrackingUserModel(){}

    public HTrackingUserModel(String mobile, String deviceId) {
        this.mobile = mobile;
        this.deviceId = deviceId;
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
