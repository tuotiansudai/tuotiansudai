package com.tuotiansudai.message.repository.model;

public enum MessageUserGroup {
    ALL_USER("全部用户"),
    STAFF("业务员"),
    STAFF_RECOMMEND_LEVEL_ONE("业务员一级推荐用户"),
    REGISTERED_NOT_INVESTED_USER("已实名未投资用户"),
    CHANNEL_USER("渠道用户"),
    NORMAL_USER("自然用户"),
    IMPORT_USER("导入用户名单");

    private final String description;

    MessageUserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
