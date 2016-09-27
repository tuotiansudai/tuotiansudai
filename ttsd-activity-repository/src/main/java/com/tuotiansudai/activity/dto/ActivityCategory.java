package com.tuotiansudai.activity.dto;


public enum ActivityCategory {
    AUTUMN_PRIZE("旅游奢侈品活动"),
    NATIONAL_PRIZE("国庆活动");

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
