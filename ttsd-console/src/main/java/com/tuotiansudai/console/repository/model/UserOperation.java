package com.tuotiansudai.console.repository.model;

public enum UserOperation {
    BIND_CARD("绑卡"),
    FAST_PAY("开通快捷支付"),
    RECHARGED("充值"),
    INVESTED("出借"),
    MULTIPLE_INVESTED("2次出借及以上");

    private String description;

    UserOperation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
