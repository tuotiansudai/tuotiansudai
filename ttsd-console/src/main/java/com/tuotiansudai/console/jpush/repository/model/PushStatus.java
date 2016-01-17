package com.tuotiansudai.console.jpush.repository.model;

public enum PushStatus {
    SEND_SUCCESS("已发送"),
    SEND_FAIL("发送失败"),
    CREATED("已创建");

    private final String description;

    PushStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
