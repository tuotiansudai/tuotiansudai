package com.tuotiansudai.activity.dto;


public enum PrizeType {
    AUTUMN_PRIZE("旅游奢侈品活动"),
    NATIONAL_PRIZE("国庆活动");

    PrizeType(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
