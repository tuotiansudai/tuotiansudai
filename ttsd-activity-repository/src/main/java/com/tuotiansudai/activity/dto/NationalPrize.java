package com.tuotiansudai.activity.dto;

public enum NationalPrize {
    MEMBERSHIP_V5("一个月V5会员体验"),
    RED_ENVELOPE_15("15元投资红包"),
    RED_ENVELOPE_50("50元投资红包"),
    TELEPHONE_FARE_10("10元话费"),
    IQIYI_MEMBERSHIP("一个月爱奇艺会员"),
    CINEMA_TICKET("电影票一张"),
    PORCELAIN_CUP("青花瓷杯子"),
    IPHONE_7("iphone7");


    String description;

    NationalPrize(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
