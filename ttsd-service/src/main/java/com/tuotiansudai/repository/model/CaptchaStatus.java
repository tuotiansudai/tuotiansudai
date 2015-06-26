package com.tuotiansudai.repository.model;

public enum CaptchaStatus {

    INACTIVE("inactive"),

    ACTIVATED("activated");

    private String status;

    private CaptchaStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
