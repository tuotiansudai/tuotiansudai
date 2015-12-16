package com.tuotiansudai.repository.model;

public enum SystemBillBusinessType {

    ACTIVITY_REWARD("活动奖励"),

    BIND_BANK_CARD("绑卡"),

    REPLACE_BANK_CARD("绑卡"),

    RECHARGE_SUCCESS("充值"),

    WITHDRAW_SUCCESS("提现"),

    INVEST_FEE("利息管理费"),

    REFERRER_REWARD("推荐人奖励"),

    LOAN_REMAINING_INTEREST("标的剩余利息"),

    NEWBIE_COUPON("体验券");

    private final String description;

    SystemBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
