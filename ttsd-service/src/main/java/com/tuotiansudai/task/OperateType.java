package com.tuotiansudai.task;

public enum OperateType {

    PROJECT("项目"),

    ACTIVITY("活动"),

    USER("用户"),

    PUSH("推送");

    private String name;

    OperateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
