package com.tuotiansudai.enums.riskestimation;

//年龄
public enum Age {
    _18_30(4, "18岁-30岁"),
    _31_50(3, "31岁-50岁"),
    _51_60(2,"51岁-60岁"),
    GREATER_THAN_60(1, "60岁以上");

    private final int points;

    private final String desc;

    Age(int points, String desc) {
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
