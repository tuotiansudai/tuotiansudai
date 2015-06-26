package com.tuotiansudai.repository.model;

public enum UserStatus {
    INACTIVE("inactive"),
    ACTIVE("active");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }
}
