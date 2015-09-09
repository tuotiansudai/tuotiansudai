package com.tuotiansudai.repository.model;

public enum SystemBillBusinessType {
    BINDING_CARD("绑卡"),
    GIVE_MONEY_TO_BORROWER("放款"),
    RECHARGE_SUCCESS("充值"),
    WITHDRAW_success("提现"),
    REFERRER_REWARD("推荐人奖励");
    private final String description;

    SystemBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
