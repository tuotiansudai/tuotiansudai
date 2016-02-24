package com.tuotiansudai.point.repository.model;

public enum PointBusinessType {
    SIGN_IN("签到奖励"),
    EXCHANGE("兑换"),
    INVEST("投资奖励"),
    TASK("任务奖励");

    private final String description;

    PointBusinessType(String description) {
        this.description = description;
    }
}
