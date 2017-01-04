package com.tuotiansudai.message.repository.model;

public enum MessageUserGroup {
    ALL_USER("全部用户"),
    IMPORT_USER("导入用户名单");

    private final String description;

    MessageUserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
