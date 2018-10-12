package com.tuotiansudai.activity.repository.model;


public enum ActivityDrawLotteryTask {
    REGISTER("注册", 1),
    EACH_REFERRER("邀请好友送一次", 1),
    EACH_REFERRER_INVEST("邀请好友后出借", 1),
    CERTIFICATION("实名认证", 1),
    BANK_CARD("绑卡", 1),
    RECHARGE("充值", 1),
    INVEST("出借", 1),
    EACH_ACTIVITY_SIGN_IN("活动签到", 1),
    TODAY_ACTIVITY_SIGN_IN("当日活动签到", 1),
    REFERRER_USER("邀请好友送5次机会", 5),
    FIRST_INVEST("首次出借", 1),
    EACH_INVEST_1000("每出借1000", 1),
    EACH_INVEST_2000("每出借2000", 1),
    EACH_INVEST_5000("每出借5000", 1),
    EACH_INVEST_10000("每出借10000", 1),
    EACH_EVERY_DAY("每天一次", 1),
    DOUBLE_ELEVEN_INVEST("双11出借", 1);

    ActivityDrawLotteryTask(String description, int time) {
        this.description = description;
        this.time = time;
    }

    private String description;

    private int time;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
