package com.tuotiansudai.enums;

public enum BillOperationType {
    IN("资金转入"),
    OUT("资金转出");

    private final String description;

    BillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
