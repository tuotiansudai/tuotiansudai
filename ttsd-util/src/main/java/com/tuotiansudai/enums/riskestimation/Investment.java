package com.tuotiansudai.enums.riskestimation;

//可用于金融出借的比例
public enum Investment {
    LESS_THAN_10(1, "小于10%", 0.1),
    _10_25(2, "10%至25%", 0.25),
    _25_50(3, "25%至50%", 0.5),
    GREATER_THAN_50(4, "大于50%", 0.6);

    private final int points;

    private final String desc;

    private final double value;

    Investment(int points, String desc, double value) {
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

    public double getValue() {
        return value;
    }
}
