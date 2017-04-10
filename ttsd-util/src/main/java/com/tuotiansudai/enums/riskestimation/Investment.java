package com.tuotiansudai.enums.riskestimation;

//可用于金融投资的比例
public enum Investment {
    LESS_THAN_10(1, "小于10%"),
    _10_25(2, "10%至25%"),
    _25_50(3, "25%至50%"),
    GREATER_THAN_50(4, "大于50%");

    private final int points;

    private final String desc;

    Investment(int points, String desc) {
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
