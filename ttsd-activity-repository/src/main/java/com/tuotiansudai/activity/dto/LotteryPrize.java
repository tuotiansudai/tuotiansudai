package com.tuotiansudai.activity.dto;

public enum LotteryPrize{
    TOURISM("华东旅游大奖",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE),
    MANGO_CARD_100("100元芒果卡",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE),
    LUXURY("奢侈品大奖",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE),
    PORCELAIN_CUP("青花瓷杯子",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE),
    RED_ENVELOPE_100("100元现金红包",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE),
    RED_ENVELOPE_50("50元现金红包",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE),
    INTEREST_COUPON_5("0.5%加息券",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE),
    INTEREST_COUPON_2("0.2%加息券",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE),

    MEMBERSHIP_V5("一个月V5会员体验",PrizeType.VIRTUAL,ActivityCategory.NATIONAL_PRIZE),
    RED_INVEST_15("15元投资红包",PrizeType.VIRTUAL,ActivityCategory.NATIONAL_PRIZE),
    RED_INVEST_50("50元投资红包",PrizeType.VIRTUAL,ActivityCategory.NATIONAL_PRIZE),
    TELEPHONE_FARE_10("10元话费",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE),
    IQIYI_MEMBERSHIP("一个月爱奇艺会员",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE),
    CINEMA_TICKET("电影票一张",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE),
    FLOWER_CUP("青花瓷杯子",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE),
    IPHONE_7("iphone7",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE);

    String description;
    PrizeType prizeType;
    ActivityCategory activityCategory;

    LotteryPrize(String description,PrizeType prizeType,ActivityCategory activityCategory){
        this.description = description;
        this.activityCategory = activityCategory;
        this.prizeType = prizeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PrizeType getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(PrizeType prizeType) {
        this.prizeType = prizeType;
    }

    public ActivityCategory getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(ActivityCategory activityCategory) {
        this.activityCategory = activityCategory;
    }
}
