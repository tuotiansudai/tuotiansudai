package com.tuotiansudai.membership.repository.model;

public enum UserMembershipType {
    UPGRADE("升级"),
    GIVEN("赠送");

    private final String description;

    UserMembershipType(String description) {
        this.description = description;
    }
}
