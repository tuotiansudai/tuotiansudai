package com.tuotiansudai.enums.riskestimation;

public enum Rate {
    GREATER_THAN_AVERAGE(1, "高于同期定期存款"),
    LESS_THAN_10(2, "10%以内，要求相对风险较低"),
    _10_15(3, "10-15%，可承受中等风险"),
    GREATER_THAN_15(4, "15%以上，可承担较高风险");

    private final int points;

    private final String desc;

    Rate(int points, String desc) {
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