package com.tuotiansudai.activity.dto;

public enum LotteryPrize{
    TOURISM("华东旅游大奖",ActivityCategory.CONCRETE,PrizeType.AUTUMN_PRIZE),
    MANGO_CARD_100("100元芒果卡",ActivityCategory.CONCRETE,PrizeType.AUTUMN_PRIZE),
    LUXURY("奢侈品大奖",ActivityCategory.CONCRETE,PrizeType.AUTUMN_PRIZE),
    PORCELAIN_CUP("青花瓷杯子",ActivityCategory.CONCRETE,PrizeType.AUTUMN_PRIZE),
    RED_ENVELOPE_100("100元现金红包",ActivityCategory.VIRTUAL,PrizeType.AUTUMN_PRIZE),
    RED_ENVELOPE_50("50元现金红包",ActivityCategory.VIRTUAL,PrizeType.AUTUMN_PRIZE),
    INTEREST_COUPON_5("0.5%加息券",ActivityCategory.VIRTUAL,PrizeType.AUTUMN_PRIZE),
    INTEREST_COUPON_2("0.2%加息券",ActivityCategory.VIRTUAL,PrizeType.AUTUMN_PRIZE),

    MEMBERSHIP_V5("一个月V5会员体验",ActivityCategory.VIRTUAL,PrizeType.NATIONAL_PRIZE),
    RED_INVEST_15("15元投资红包",ActivityCategory.VIRTUAL,PrizeType.NATIONAL_PRIZE),
    RED_INVEST_50("50元投资红包",ActivityCategory.VIRTUAL,PrizeType.NATIONAL_PRIZE),
    TELEPHONE_FARE_10("10元话费",ActivityCategory.CONCRETE,PrizeType.NATIONAL_PRIZE),
    IQIYI_MEMBERSHIP("一个月爱奇艺会员",ActivityCategory.CONCRETE,PrizeType.NATIONAL_PRIZE),
    CINEMA_TICKET("电影票一张",ActivityCategory.CONCRETE,PrizeType.NATIONAL_PRIZE),
    FLOWER_CUP("青花瓷杯子",ActivityCategory.CONCRETE,PrizeType.NATIONAL_PRIZE),
    IPHONE_7("iphone7",ActivityCategory.CONCRETE,PrizeType.NATIONAL_PRIZE);

    String description;
    ActivityCategory activityCategory;
    PrizeType prizeType;

    LotteryPrize(String description,ActivityCategory activityCategory,PrizeType prizeType){
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

    public ActivityCategory getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(ActivityCategory activityCategory) {
        this.activityCategory = activityCategory;
    }

    public PrizeType getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(PrizeType prizeType) {
        this.prizeType = prizeType;
    }
}
