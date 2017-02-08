package com.tuotiansudai.repository.model;

public enum RechargeSource {

    WEB("WEB"),
    IOS("IOS"),
    ANDROID("ANDROID");

    private final String description;

    RechargeSource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
