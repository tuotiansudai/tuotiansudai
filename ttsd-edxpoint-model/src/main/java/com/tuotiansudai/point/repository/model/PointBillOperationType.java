package com.tuotiansudai.point.repository.model;


public enum PointBillOperationType {
    IN("入账"),

    OUT("出账");

    private final String description;

    PointBillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
