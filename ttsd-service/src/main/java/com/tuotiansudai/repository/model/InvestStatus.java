package com.tuotiansudai.repository.model;

public enum InvestStatus {
    WAITING("等待确认"),
    SUCCESS("投资成功"),
    FAIL("投资失败");

    private final String description;

    InvestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
