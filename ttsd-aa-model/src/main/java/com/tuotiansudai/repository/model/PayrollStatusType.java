package com.tuotiansudai.repository.model;

public enum PayrollStatusType {
    PENDING("待审核"),
    REJECTED("被驳回"),
    AUDITED("已审待发"),
    SUCCESS("已发放"),
    FAIL("发放失败");

    private final String description;

    PayrollStatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
