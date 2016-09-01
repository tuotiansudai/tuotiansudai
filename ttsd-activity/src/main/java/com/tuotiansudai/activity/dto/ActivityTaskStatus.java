package com.tuotiansudai.activity.dto;


public enum ActivityTaskStatus {
    COMPLETE("完成"),
    PROCEED("进行中"),
    UNFINISHED("未完成");

    String description;

    ActivityTaskStatus(String description){
        this.description = description;
    }
}
