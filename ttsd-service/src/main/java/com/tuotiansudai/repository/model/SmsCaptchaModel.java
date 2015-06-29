package com.tuotiansudai.repository.model;

import java.util.Date;

public class SmsCaptchaModel {
    private Integer id;

    private String code;

    private String mobile;

    private Date deadLine;

    private Date generationTime;

    private CaptchaStatus status;

    private CaptchaType captchaType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(Date generationTime) {
        this.generationTime = generationTime;
    }

    public CaptchaStatus getStatus() {
        return status;
    }

    public void setStatus(CaptchaStatus status) {
        this.status = status;
    }


    public CaptchaType getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }
}
