package com.tuotiansudai.api.dto;

import java.io.Serializable;

public class LoginRequestDto implements Serializable{

    private String j_username;

    private String j_password;

    private String j_source;

    private String j_deviceId;

    private String captcha;

    private String mobile;

    private String deviceId;

    private String source;

    private String password;

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String j_username) {
        this.j_username = j_username;
    }

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String j_password) {
        this.j_password = j_password;
    }

    public String getJ_source() {
        return j_source;
    }

    public void setJ_source(String j_source) {
        this.j_source = j_source;
    }

    public String getJ_deviceId() {
        return j_deviceId;
    }

    public void setJ_deviceId(String j_deviceId) {
        this.j_deviceId = j_deviceId;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
