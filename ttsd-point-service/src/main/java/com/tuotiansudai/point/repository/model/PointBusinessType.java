package com.tuotiansudai.point.repository.model;

public enum PointBusinessType {
    SIGN_IN("签到奖励"),
    TASK("任务奖励"),
    EXCHANGE("积分兑换"),
    INVEST("投资奖励"),
    LOTTERY("抽奖"),
    POINT_CLEAR("积分清零"),
    ACTIVITY("活动奖励");

    private final String description;

    PointBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
