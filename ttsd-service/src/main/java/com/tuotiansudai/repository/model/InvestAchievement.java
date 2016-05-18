package com.tuotiansudai.repository.model;

public enum InvestAchievement {
    FIRST_INVEST("拓荒先锋"),
    LAST_INVEST("一锤定音"),
    MAX_AMOUNT("拓天标王");

    private final String description;

    InvestAchievement(String description) {
        this.description = description;
    }
}
