package com.tuotiansudai.activity.dto;

public enum LotteryPrize {
    TOURISM("华东旅游大奖"),
    MANGO_CARD_100("100元芒果卡"),
    RED_ENVELOPE_100("100元现金红包"),
    RED_ENVELOPE_50("50元现金红包"),
    INTEREST_COUPON_5("0.5加息券"),
    INTEREST_COUPON_2("0.2加息券");

    String description;

    LotteryPrize(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
