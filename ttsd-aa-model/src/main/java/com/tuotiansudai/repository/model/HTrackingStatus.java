package com.tuotiansudai.repository.model;

public enum HTrackingStatus {
    UN_REGISTER("未注册"),
    REGISTERED("已注册");

    String description;

    HTrackingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
