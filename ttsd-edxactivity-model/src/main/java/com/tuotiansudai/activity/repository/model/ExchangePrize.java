package com.tuotiansudai.activity.repository.model;


public enum ExchangePrize {

    //运动奖&职业奖 兑换
    HOUSEHOLE_DUMBBELL("家用哑铃一对",ActivityCategory.EXERCISE_WORK_ACTIVITY,5000000),
    USB_LIGHTER("USB充电打火机",ActivityCategory.EXERCISE_WORK_ACTIVITY,5000000),
    CAR_REFRIGRRATOR("科敏车载冰箱",ActivityCategory.EXERCISE_WORK_ACTIVITY,10000000),
    MOBILE_HDD_500G("东芝移动硬盘500G",ActivityCategory.EXERCISE_WORK_ACTIVITY,10000000),
    ARCTIC_WOLF_TENT("北极狼自动帐篷",ActivityCategory.EXERCISE_WORK_ACTIVITY,12000000),
    GOLF_MAN_BRIEFCASE("GOLF男士商务公文包",ActivityCategory.EXERCISE_WORK_ACTIVITY,12000000),
    MUTE_SPINNING("伊吉康室内静音动感单车",ActivityCategory.EXERCISE_WORK_ACTIVITY,28000000),
    WEIGHTING_DRAW_BAR_BOX("美而美智能称重拉杆箱",ActivityCategory.EXERCISE_WORK_ACTIVITY,28000000),
    MANGO_TOURISM_CARD_2000("2000元芒果旅游卡",ActivityCategory.EXERCISE_WORK_ACTIVITY,38000000),
    SMARTISAN_NUTS_PRO("锤子坚果pro",ActivityCategory.EXERCISE_WORK_ACTIVITY,38000000),
    APPLE_WATCH_SERIES_2("Apple Watch Series 2",ActivityCategory.EXERCISE_WORK_ACTIVITY,60000000),
    APPLE_IPAD_128G("Apple iPad 128G",ActivityCategory.EXERCISE_WORK_ACTIVITY,60000000),

    //惊喜不重样加息不打烊活动
    JD_E_CARD("200元京东E卡", ActivityCategory.START_WORK_ACTIVITY, 5000000),
    ELECTRIC_TOOTHBRUSH("拜尔智能电动牙刷 情侣双人", ActivityCategory.START_WORK_ACTIVITY, 10000000),
    FOREO_MINI2("Foreo露娜 mini2", ActivityCategory.START_WORK_ACTIVITY, 20000000),
    XIAOMI_NOTE3("小米note3美颜双摄拍照手机", ActivityCategory.START_WORK_ACTIVITY, 30000000),
    WMF_FUTENG("WMF 福腾宝德国原装进口六件套组合", ActivityCategory.START_WORK_ACTIVITY, 40000000),
    XIAOMI_TV("小米(MI)电视4A标准版55英寸", ActivityCategory.START_WORK_ACTIVITY, 50000000),
    APPLE_MACBOOK("Apple MacBook Air 11.6英寸笔记本电脑", ActivityCategory.START_WORK_ACTIVITY, 75000000),
    APPLE_IPONEX("Apple IPhoneX 64GB", ActivityCategory.START_WORK_ACTIVITY, 100000000),
    ;

    String prizeName;
    ActivityCategory activityCategory;
    long exchangeMoney;

    ExchangePrize(String prizeName,ActivityCategory activityCategory,long exchangeMoney){
        this.prizeName=prizeName;
        this.activityCategory=activityCategory;
        this.exchangeMoney=exchangeMoney;
    }


    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public ActivityCategory getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(ActivityCategory activityCategory) {
        this.activityCategory = activityCategory;
    }

    public long getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(long exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }
}
