package com.tuotiansudai.message.repository.model;

public enum MessageCategory {
    SYSTEM("系统消息"),
    NOTIFY("拓天公告"),
    ACTIVITY("活动通知");

    private String description;

    MessageCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
