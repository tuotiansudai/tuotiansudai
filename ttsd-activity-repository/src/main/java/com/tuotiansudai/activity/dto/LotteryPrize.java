package com.tuotiansudai.activity.dto;

public enum LotteryPrize {
    TOURISM("华东旅游大奖","real"),
    MANGO_CARD_100("100元芒果卡","real"),
    LUXURY("奢侈品大奖","real"),
    PORCELAIN_CUP("青花瓷杯子","real"),
    RED_ENVELOPE_100("100元现金红包","virtual"),
    RED_ENVELOPE_50("50元现金红包","virtual"),
    INTEREST_COUPON_5("0.5加息券","virtual"),
    INTEREST_COUPON_2("0.2加息券","virtual");

    String description;
    String type;

    LotteryPrize(String description,String type){
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
