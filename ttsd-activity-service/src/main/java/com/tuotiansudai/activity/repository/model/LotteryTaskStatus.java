package com.tuotiansudai.activity.repository.model;


public enum LotteryTaskStatus {
    COMPLETE("完成"),
    PROCEED("进行中"),
    UNFINISHED("未完成");

    String description;

    LotteryTaskStatus(String description){
        this.description = description;
    }
}
