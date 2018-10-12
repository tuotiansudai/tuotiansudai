package com.tuotiansudai.enums.riskestimation;

public enum Attitude {
    OPTION_1(1, "厌恶风险，不希望本金损失，希望获得稳定回报"),
    OPTION_2(2, "保守出借，不希望本金损失，愿意承担一定幅度的收益波动"),
    OPTION_3(3, "寻求资金的较高收益和成长性，愿意为此承担有限本金损失"),
    OPTION_4(4, "希望赚取高回报，愿意为此承担较大本金损失");

    private final int points;

    private final String desc;

    Attitude(int points, String desc) {
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

