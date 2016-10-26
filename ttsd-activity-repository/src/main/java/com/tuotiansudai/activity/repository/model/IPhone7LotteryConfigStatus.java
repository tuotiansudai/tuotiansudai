package com.tuotiansudai.activity.repository.model;

public enum IPhone7LotteryConfigStatus {
    TO_APPROVE("待审核"),
    APPROVED("已审核"),
    REFUSED("已驳回"),
    EFFECTIVE("已生效");

    private String description;

    IPhone7LotteryConfigStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}