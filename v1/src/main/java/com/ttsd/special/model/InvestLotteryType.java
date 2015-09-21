package com.ttsd.special.model;

public enum InvestLotteryType {

    /**
     * 新手标
     */
    NOVICE("新手标"),
    /**
     * 普通标
     */
    NORMAL("普通标");

    private final String desc;

    InvestLotteryType (String desc) {
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
