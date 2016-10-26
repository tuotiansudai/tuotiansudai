package com.tuotiansudai.activity.repository.model;

public enum IPhone7InvestLotteryStatus {
    WINNING("已中奖"),
    WAITING("待开奖");

    private String description;

    IPhone7InvestLotteryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}