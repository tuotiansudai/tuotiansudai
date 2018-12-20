package com.tuotiansudai.enums;

import java.io.Serializable;

public enum OperationType implements Serializable {

    PROJECT("标的", "创建标的"),

    COUPON("优惠券", "创建优惠券"),

    PAYROLL("代发工资", "代发工资"),

    USER("用户", "修改用户"),

    TRANSFER_RULE("债权转让", "修改债权转让规则"),

    ACTIVITY("活动", "创建活动"),

    BANNER("banner", "banner操作"),

    PUSH("App推送", "新建App推送"),

    BAND_CARD("绑卡管理", "终止换卡申请"),

    CONSUME_LOAN("消费借款", "消费借款");

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
