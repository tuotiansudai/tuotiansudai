package com.tuotiansudai.repository.model;


public enum CaptchaType {

    REGISTERCAPTCHA("registercaptcha");

    private String type;

    private CaptchaType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return this.type;
    }
}
