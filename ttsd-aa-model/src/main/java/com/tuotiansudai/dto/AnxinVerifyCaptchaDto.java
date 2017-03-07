package com.tuotiansudai.dto;

import java.io.Serializable;

public class AnxinVerifyCaptchaDto implements Serializable {

    private String loginName;
    private String ip;
    private String captcha;
    private boolean skipAuth;

    public AnxinVerifyCaptchaDto() {
    }

    public AnxinVerifyCaptchaDto(String loginName, String ip, String captcha, boolean skipAuth) {
        this.loginName = loginName;
        this.ip = ip;
        this.captcha = captcha;
        this.skipAuth = skipAuth;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(boolean skipAuth) {
        this.skipAuth = skipAuth;
    }
}
