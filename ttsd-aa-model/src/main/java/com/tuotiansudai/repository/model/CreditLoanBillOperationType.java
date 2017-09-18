package com.tuotiansudai.repository.model;

public enum CreditLoanBillOperationType {

    IN("入账"),

    OUT("出账");

    private final String description;

    CreditLoanBillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
