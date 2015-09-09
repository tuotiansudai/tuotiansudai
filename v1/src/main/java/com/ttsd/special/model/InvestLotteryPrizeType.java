package com.ttsd.special.model;

public enum InvestLotteryPrizeType {
    A("1000元礼品"),
    B("500元礼品"),
    C("300元礼品"),
    D("100元礼品"),
    E("代金券"),
    F("礼品卷"),
    G("现金");

    private final String desc;

    InvestLotteryPrizeType (String desc) {
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
