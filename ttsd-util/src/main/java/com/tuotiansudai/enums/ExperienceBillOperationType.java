package com.tuotiansudai.enums;

public enum ExperienceBillOperationType {
    IN("入账"),
    OUT("出账");

    private final String description;

    ExperienceBillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
