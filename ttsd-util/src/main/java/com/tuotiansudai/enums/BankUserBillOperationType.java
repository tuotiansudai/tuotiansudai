package com.tuotiansudai.enums;

public enum BankUserBillOperationType {
    IN("可用余额转入"),
    OUT("可用余额转出");

    private final String description;

    BankUserBillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
