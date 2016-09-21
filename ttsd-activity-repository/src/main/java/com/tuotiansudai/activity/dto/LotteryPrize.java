package com.tuotiansudai.activity.dto;

public enum LotteryPrize{
    TOURISM("华东旅游大奖","real"),
    MANGO_CARD_100("100元芒果卡","real"),
    LUXURY("奢侈品大奖","real"),
    PORCELAIN_CUP("青花瓷杯子","real"),
    RED_ENVELOPE_100("100元现金红包","virtual"),
    RED_ENVELOPE_50("50元现金红包","virtual"),
    INTEREST_COUPON_5("0.5%加息券","virtual"),
    INTEREST_COUPON_2("0.2%加息券","virtual"),

    MEMBERSHIP_V5("一个月V5会员体验","virtual"),
    RED_INVEST_15("15元投资红包","virtual"),
    RED_INVEST_50("50元投资红包","virtual"),
    TELEPHONE_FARE_10("10元话费","real"),
    IQIYI_MEMBERSHIP("一个月爱奇艺会员","real"),
    CINEMA_TICKET("电影票一张","real"),
    FLOWER_CUP("青花瓷杯子","real"),
    IPHONE_7("iphone7","real");

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
