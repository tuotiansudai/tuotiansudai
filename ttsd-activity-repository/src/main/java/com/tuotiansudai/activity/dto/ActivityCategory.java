package com.tuotiansudai.activity.dto;


public enum ActivityCategory {
    CONCRETE("真实奖品"),
    VIRTUAL("虚拟奖品");

    ActivityCategory(String description) {
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
