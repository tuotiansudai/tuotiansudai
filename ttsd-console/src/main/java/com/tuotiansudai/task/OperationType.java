package com.tuotiansudai.task;

import java.io.Serializable;

public enum OperationType implements Serializable{

    PROJECT("创建标的"),

    ACTIVITY("创建优惠券"),

    USER("修改用户"),

    PUSH("新建推送");

    private String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
