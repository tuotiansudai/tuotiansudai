package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class LoginLogModel implements Serializable {
    private long id;

    private String loginName;

    private Source source;

    private String ip;

    private String device;

    private Date loginTime;

    private boolean success;

    public LoginLogModel() {
    }

    public LoginLogModel(String loginName, Source source, String ip, String device, boolean success) {
        this.loginName = loginName;
        this.source = source;
        this.ip = ip;
        this.device = device;
        this.loginTime = new Date();
        this.success = success;
    }

    public long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public Source getSource() {
        return source;
    }

    public String getIp() {
        return ip;
    }

    public String getDevice() {
        return device;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public boolean isSuccess() {
        return success;
    }
}
