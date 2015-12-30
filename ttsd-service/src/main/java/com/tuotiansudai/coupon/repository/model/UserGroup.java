package com.tuotiansudai.coupon.repository.model;

public enum UserGroup {
    INVESTED_USER("已投资用户"),
    REGISTERED_NOT_INVESTED_USER("已实名未投资用户");

    private final String description;

    UserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
