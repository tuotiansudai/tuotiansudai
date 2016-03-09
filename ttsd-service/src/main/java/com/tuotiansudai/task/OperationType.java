package com.tuotiansudai.task;

import java.io.Serializable;

public enum OperationType implements Serializable{

    PROJECT("标的", "创建标的"),

    COUPON("优惠券", "创建优惠券"),

    USER("用户", "修改用户"),

    PUSH("App推送", "新建推送");

    private String targetType;

    private String description;

    OperationType(String targetType, String description) {
        this.targetType = targetType;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
