package com.tuotiansudai.jpush.repository.model;

public enum PushType {
    BIRTHDAY_ALERT_MONTH("AUTO","生日月加息活动提醒"),
    PREHEAT("MANUAL","预热标的"),
    PRESENT_SEND("MANUAL","礼品派送"),
    IMPORTANT_EVENT("MANUAL","重大事件"),
    HUMANISTIC_CARE("MANUAL","人文关怀");

    private final String description;

    private final String type;

    PushType(String type ,String description){
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

}
