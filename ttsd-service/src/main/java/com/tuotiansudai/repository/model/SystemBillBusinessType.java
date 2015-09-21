package com.tuotiansudai.repository.model;

public enum SystemBillBusinessType {

    BIND_CARD("绑卡"),

    RECHARGE_SUCCESS("充值"),

    WITHDRAW_SUCCESS("提现"),

    REFERRER_REWARD("推荐人奖励");

    private final String description;

    SystemBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
