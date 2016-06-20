package com.tuotiansudai.message.repository.model;

public enum MessageChannel {
    WEBSITE("站内信");

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    MessageChannel(String description){this.description = description; }
}
