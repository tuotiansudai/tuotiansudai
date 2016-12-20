package com.tuotiansudai.message.repository.model;

public enum MessageStatus {
    TO_APPROVE("待审核"),
    REJECTION("已驳回"),
    APPROVED("已审核");

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    MessageStatus(String description) {
        this.description = description;
    }
}
