package com.tuotiansudai.activity.repository.model;


public enum PrizeType {
    CONCRETE("真实奖品"),
    VIRTUAL("虚拟奖品"),
    MEMBERSHIP("会员"),
    EXPERIENCE("体验金"),
    POINT("积分");

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
