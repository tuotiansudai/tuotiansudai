package com.tuotiansudai.repository.model;


public enum CaptchaType {

    MOBILECAPTCHA("mobilecaptcha");

    private String type;

    private CaptchaType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return this.type;
    }
}
