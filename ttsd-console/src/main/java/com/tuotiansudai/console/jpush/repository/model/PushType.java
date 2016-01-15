package com.tuotiansudai.console.jpush.repository.model;

public enum PushType {
    PREHEAT("预热标的"),
    PRESENT_SEND("礼品派送"),
    IMPORTANT_EVENT("重大事件"),
    HUMANISTIC_CARE("人文关怀");

    private final String description;

    PushType(String description){
        this.description = description;
    }

}
