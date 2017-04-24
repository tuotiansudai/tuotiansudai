package com.tuotiansudai.enums.riskestimation;

public enum Duration {
    LESS_THAN_1(1, "1年以下"),
    _1_3(2, "1-3年"),
    _3_5(3, "3-5年"),
    GREATER_THAN_5(4, "5年以上");

    private final int points;

    private final String desc;

    Duration(int points, String desc) {
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
