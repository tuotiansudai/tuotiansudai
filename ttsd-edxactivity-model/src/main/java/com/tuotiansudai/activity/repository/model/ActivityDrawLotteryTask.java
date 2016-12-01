package com.tuotiansudai.activity.repository.model;


public enum  ActivityDrawLotteryTask {
    REGISTER_TASK("活动期间内注册"),
    REFERRER_USER("活动期间内邀请好友"),
    ACCOUNT_USER("活动期间内邀请好友"),
    ;


    ActivityDrawLotteryTask(String description){
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
