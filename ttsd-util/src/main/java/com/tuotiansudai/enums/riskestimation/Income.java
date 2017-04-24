package com.tuotiansudai.enums.riskestimation;

//家庭收入
public enum Income {
    LESS_THAN_8(0, "8万元以下", 8),
    _8_15(1, "8万-15万", 15),
    _15_30(2, "15万-30万", 30),
    _30_50(3, "30万-50万", 50),
    GREATER_THAN_50(4, "50万以上", 80);

    private final int points;

    private final String desc;

    private final int value;

    Income(int points, String desc, int value) {
        this.points = points;
        this.desc = desc;
        this.value = value;
    }

    public int getPoints() {
        return points;
    }

    public String getDesc() {
        return desc;
    }

    public int getValue() {
        return value;
    }
}
