package com.tuotiansudai.activity.dto;


public enum PromotionStatus {
    TO_APPROVED("待审核"),
    REJECTION("未通过"),
    APPROVED("已审核");

    PromotionStatus(String description) {
        this.description = description;
    }

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
