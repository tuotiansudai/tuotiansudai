package com.tuotiansudai.enums.riskestimation;

//家庭收入
public enum Income {
    LESS_THAN_8(0, "8万元以下"),
    _8_15(1, "8万-15万"),
    _15_30(2, "15万-30万"),
    _30_50(3, "30万-50万"),
    GREATER_THAN_50(4, "50万以上");

    private final int points;

    private final String desc;

    Income(int points, String desc) {
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
