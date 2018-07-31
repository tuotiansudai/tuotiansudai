package com.tuotiansudai.enums;

public enum BillOperationType {
    IN("入账"),
    OUT("出账");

    private final String description;

    BillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
