package com.tuotiansudai.repository.model;

public enum ActivityStatus {
    TO_APPROVE("待审核"),
    TO_APPROVED("已审核"),
    OPERATING("进行中"),
    REJECTION("已驳回"),
    EXPIRED("已结束");

    private String description;

    ActivityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
