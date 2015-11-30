package com.ttsd.special.model;

public enum ReceiveStatus {
    NOT_RECEIVED("未发放"),
    RECEIVED("已发放"),
    FAILED("发放失败");

    private final String desc;

    ReceiveStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
