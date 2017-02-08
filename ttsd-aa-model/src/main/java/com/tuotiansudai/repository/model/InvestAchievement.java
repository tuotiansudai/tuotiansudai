package com.tuotiansudai.repository.model;

public enum InvestAchievement {
    MAX_AMOUNT("拓天标王", 1),
    LAST_INVEST("一锤定音", 2),
    FIRST_INVEST("拓荒先锋", 3);

    private final String description;

    private final int priority;

    InvestAchievement(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
