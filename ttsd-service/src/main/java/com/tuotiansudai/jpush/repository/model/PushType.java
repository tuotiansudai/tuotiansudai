package com.tuotiansudai.jpush.repository.model;

public enum PushType {
    BIRTHDAY_ALERT("生日提醒"),
    PREHEAT("预热标的"),
    PRESENT_SEND("礼品派送"),
    IMPORTANT_EVENT("重大事件"),
    HUMANISTIC_CARE("人文关怀");

    private final String description;

    PushType(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
