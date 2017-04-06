package com.tuotiansudai.enums.riskestimation;

public enum Loss {
    LESS_THAN_10(1, "10%以内"),
    _10_30(2, "10-30%"),
    _30_50(3, "30-50%"),
    GREATER_THAN_50(4, "50%以上");

    private final int points;

    private final String desc;

    Loss(int points, String desc) {
        this.points = points;
        this.desc = desc;
    }

    public int getPoints() {
        return points;
    }

    public String getDesc() {
        return desc;
    }
}
