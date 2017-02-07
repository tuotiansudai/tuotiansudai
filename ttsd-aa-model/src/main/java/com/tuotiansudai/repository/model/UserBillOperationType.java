package com.tuotiansudai.repository.model;

public enum UserBillOperationType {
    TI_BALANCE("可用余额转入"),
    TO_BALANCE("可用余额转出"),
    FREEZE("冻结金额"),
    UNFREEZE("解冻金额"),
    TO_FREEZE("冻结金额转出");

    private final String description;

    UserBillOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
