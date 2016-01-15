package com.tuotiansudai.coupon.repository.model;

public enum UserGroup {
    NEW_REGISTERED_USER("新注册用户"),
    INVESTED_USER("已投资用户"),
    REGISTERED_NOT_INVESTED_USER("已实名未投资用户");

    private final String description;

    UserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInvestedUser(){
        return this == INVESTED_USER;
    }
    public boolean isRegisteredNotInvestedUser(){
        return this == REGISTERED_NOT_INVESTED_USER;
    }
}