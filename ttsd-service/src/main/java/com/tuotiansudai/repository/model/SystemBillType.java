package com.tuotiansudai.repository.model;

public enum SystemBillType {
    IN("in"),
    OUT("out");

    private final String description;

    SystemBillType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
