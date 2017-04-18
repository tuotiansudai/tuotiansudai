package com.tuotiansudai.enums.riskestimation;

public enum Experience {
    NONE(0, "没有经验"),
    LESS_THAN_2(1, "少于2年"),
    _2_5(2, "2至5年"),
    _5_8(3, "5至8年"),
    GREATER_THAN_8(4, "8年以上");

    private final int points;

    private final String desc;

    Experience(int points, String desc) {
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

