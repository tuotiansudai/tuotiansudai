package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class SmsCaptchaModel implements Serializable {
    private Long id;

    private String captcha;

    private String mobile;

    private Date expiredTime;

    private Date createdTime;

    private CaptchaType captchaType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public CaptchaType getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }
}
