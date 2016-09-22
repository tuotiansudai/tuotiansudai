package com.tuotiansudai.activity.dto;


public enum PrizeType {
    CONCRETE("真实奖品"),
    VIRTUAL("虚拟奖品");

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
