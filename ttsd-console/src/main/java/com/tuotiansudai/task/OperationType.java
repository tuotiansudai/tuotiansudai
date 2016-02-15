package com.tuotiansudai.task;

import java.io.Serializable;

public enum OperationType implements Serializable{

    PROJECT("项目"),

    ACTIVITY("活动"),

    USER("用户"),

    PUSH("推送");

    private String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
