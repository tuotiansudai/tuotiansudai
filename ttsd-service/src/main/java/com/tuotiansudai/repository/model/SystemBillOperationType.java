package com.tuotiansudai.repository.model;

public enum SystemBillOperationType {

    IN("入账"),

    OUT("出账");

    private final String description;

    SystemBillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
