package com.tuotiansudai.membership.repository.model;

public enum UserMembershipType {
    ALL("全部"),
    UPGRADE("升级"),
    GIVEN("赠送");
    private final String description;

    UserMembershipType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
