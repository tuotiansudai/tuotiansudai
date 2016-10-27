package com.tuotiansudai.activity.repository.model;

public enum LotteryPrize {
    //旅游奢侈品活动
    TOURISM("华东旅游大奖",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE,0),
    MANGO_CARD_100("100元芒果卡",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE,0.5),
    LUXURY("奢侈品大奖",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE,0),
    PORCELAIN_CUP("青花瓷杯子",PrizeType.CONCRETE,ActivityCategory.AUTUMN_PRIZE,0.5),
    RED_ENVELOPE_100("100元现金红包",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE,20),
    RED_ENVELOPE_50("50元现金红包",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE,25),
    INTEREST_COUPON_5("0.5%加息券",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE,25),
    INTEREST_COUPON_2("0.2%加息券",PrizeType.VIRTUAL,ActivityCategory.AUTUMN_PRIZE,29.5),

    //1000积分抽奖
    BICYCLE_XM("小米（MI）九号平衡车",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_1000,0),
    MASK("防雾霾骑行口罩",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_1000,3),
    LIPSTICK("屈臣氏润唇膏",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_1000,3),
    PORCELAIN_CUP_BY_1000("青花瓷杯子",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_1000,1.5),
    PHONE_BRACKET("懒人手机支架",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_1000,4.5),
    PHONE_CHARGE_10("10元话费",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_1000,3),
    RED_ENVELOPE_10("10元投资红包",PrizeType.VIRTUAL,ActivityCategory.POINT_DRAW_1000,45),
    INTEREST_COUPON_2_POINT_DRAW("0.2%加息券",PrizeType.VIRTUAL,ActivityCategory.POINT_DRAW_1000,40),

    //10000积分抽奖
    IPHONE7_128G("iPhone 7手机128G",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_10000,0),
    DELAYED_ACTION("通用自拍杆",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_10000,3),
    U_DISH("拓天速贷U盘",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_10000,3),
    PHONE_CHARGE_20("20元话费",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_10000,15),
    HEADREST("车家两用U型头枕",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_10000,10),
    IQIYI_MEMBERSHIP_30("爱奇艺会员月卡",PrizeType.CONCRETE,ActivityCategory.POINT_DRAW_10000,15),
    RED_ENVELOPE_50_POINT_DRAW("50元投资红包",PrizeType.VIRTUAL,ActivityCategory.POINT_DRAW_10000,24),
    INTEREST_COUPON_5_POINT_DRAW("0.5%加息券",PrizeType.VIRTUAL,ActivityCategory.POINT_DRAW_10000,30),

    //国庆活动
    MEMBERSHIP_V5("一个月V5会员体验",PrizeType.MEMBERSHIP,ActivityCategory.NATIONAL_PRIZE,25),
    RED_INVEST_15("15元投资红包",PrizeType.VIRTUAL,ActivityCategory.NATIONAL_PRIZE,30),
    RED_INVEST_50("50元投资红包",PrizeType.VIRTUAL,ActivityCategory.NATIONAL_PRIZE,30),
    TELEPHONE_FARE_10("10元话费",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE,5),
    IQIYI_MEMBERSHIP("一个月爱奇艺会员",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE,4),
    CINEMA_TICKET("电影票一张",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE,3),
    FLOWER_CUP("青花瓷杯子",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE,3),
    IPHONE_7("iphone7",PrizeType.CONCRETE,ActivityCategory.NATIONAL_PRIZE,0),

    //双11狂欢
    M1_PHONE("锤子M1手机(预定)",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    HUMIDIFIER("小熊加湿器",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    HAIR_DRIER("飞科电吹风机",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    IQIYI_MEMBERSHIP_REF_CARNIVAL("爱奇艺会员",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    TELEPHONE_FARE_10_REF_CARNIVAL("10元话费",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    BAMBOO_CHARCOAL_PACKAGE("卡通汽车竹炭包",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL("0.5加息券",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0),
    RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL("50元红包",PrizeType.VIRTUAL,ActivityCategory.CARNIVAL_ACTIVITY,0);

    String description;
    PrizeType prizeType;
    ActivityCategory activityCategory;
    double rate;

    LotteryPrize(String description, PrizeType prizeType, ActivityCategory activityCategory, double rate){
        this.description = description;
        this.activityCategory = activityCategory;
        this.prizeType = prizeType;
        this.rate = rate;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
