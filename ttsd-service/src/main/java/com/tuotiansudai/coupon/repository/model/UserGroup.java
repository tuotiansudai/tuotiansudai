package com.tuotiansudai.coupon.repository.model;

public enum UserGroup {
    ALL_USER("全部用户"),
    INVESTED_USER("已投资用户"),
    REGISTERED_NOT_INVESTED_USER("已实名未投资用户"),
    STAFF("业务员"),
    STAFF_RECOMMEND_LEVEL_ONE("业务员一代用户"),
    AGENT("代理商"),
    CHANNEL("来源渠道"),
    NEW_REGISTERED_USER("新注册用户"),
    IMPORT_USER("导入用户名单");



    private final String description;

    UserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}