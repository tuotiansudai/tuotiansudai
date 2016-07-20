package com.tuotiansudai.message.repository.model;

public enum MessageStatus {
    TO_APPROVE("待批准"),
    REJECTION("驳回"),
    APPROVED("已批准");

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    MessageStatus(String description){this.description = description; }
}
