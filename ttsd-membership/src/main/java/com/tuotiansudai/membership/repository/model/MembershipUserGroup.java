package com.tuotiansudai.membership.repository.model;

public enum MembershipUserGroup {
    NEW_REGISTERED_USER("新注册用户"),
    IMPORT_USER("导入用户名单");

    private final String description;

    MembershipUserGroup(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
