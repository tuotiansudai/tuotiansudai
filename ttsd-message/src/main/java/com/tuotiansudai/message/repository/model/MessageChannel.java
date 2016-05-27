package com.tuotiansudai.message.repository.model;

public enum MessageChannel {
    WEBSITE("站内信"),
    APP_MESSAGE("APP推送"),
    SMS("短信"),
    MAIL("邮件");

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    MessageChannel(String description){this.description = description; }
}
