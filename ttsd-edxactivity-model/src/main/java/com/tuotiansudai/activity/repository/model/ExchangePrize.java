package com.tuotiansudai.activity.repository.model;


public enum ExchangePrize {

    //运动奖&职业奖 兑换
    HOUSEHOLE_DUMBBELL("家用哑铃",ActivityCategory.EXERCISE_WORK_ACTIVITY,5000000),
    USB_LIGHTER("USB充电打火机",ActivityCategory.EXERCISE_WORK_ACTIVITY,5000000),
    CAR_REFRIGRRATOR("科敏车载冰箱",ActivityCategory.EXERCISE_WORK_ACTIVITY,10000000),
    MOBILE_HDD_1TB("西部数据移动硬盘1TB",ActivityCategory.EXERCISE_WORK_ACTIVITY,10000000),
    ARCTIC_WOLF_TENT("北极狼自动帐篷",ActivityCategory.EXERCISE_WORK_ACTIVITY,12000000),
    GOLF_MAN_BRIEFCASE("GOLF男士商务公文包",ActivityCategory.EXERCISE_WORK_ACTIVITY,12000000),
    MUTE_SPINNING("伊吉康室内静音动感单车",ActivityCategory.EXERCISE_WORK_ACTIVITY,28000000),
    WEIGHTING_DRAW_BAR_BOX("美而美智能称重拉杆箱",ActivityCategory.EXERCISE_WORK_ACTIVITY,28000000),
    MANGO_TOURISM_CARD_2000("2000元芒果旅游卡",ActivityCategory.EXERCISE_WORK_ACTIVITY,38000000),
    SMARTISAN_NUTS_PRO("锤子坚果pro",ActivityCategory.EXERCISE_WORK_ACTIVITY,38000000),
    APPLE_WATCH_SERIES_2("Apple Watch Series 2",ActivityCategory.EXERCISE_WORK_ACTIVITY,60000000),
    APPLE_IPAD_128G("Apple iPad 128G",ActivityCategory.EXERCISE_WORK_ACTIVITY,60000000);

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
