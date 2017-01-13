package com.tuotiansudai.log.repository.model;


import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;

public class UserOpLogModel {

    private long id;

    private String loginName;

    private String mobile;

    private UserOpType opType;

    private String ip;

    private String deviceId;

    private Source source;

    private String description;

    private Date createdTime;

    public UserOpLogModel() {
    }

    public UserOpLogModel(long id, String loginName, UserOpType opType, String ip, String deviceId, Source source, String description) {
        this.id = id;
        this.loginName = loginName;
        this.opType = opType;
        this.ip = ip;
        this.deviceId = deviceId;
        this.source = source;
        this.description = description;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserOpType getOpType() {
        return opType;
    }

    public void setOpType(UserOpType opType) {
        this.opType = opType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
