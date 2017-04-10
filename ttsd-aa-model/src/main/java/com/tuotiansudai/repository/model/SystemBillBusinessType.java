package com.tuotiansudai.repository.model;

public enum SystemBillBusinessType {

    ACTIVITY_REWARD("活动奖励"),

    BIND_BANK_CARD("绑卡"),

    REPLACE_BANK_CARD("换卡"),

    RECHARGE_SUCCESS("充值"),

    PUBLIC_RECHARGE_SUCCESS("企业充值"),

    WITHDRAW_SUCCESS("提现"),

    SYSTEM_RECHARGE("平台充值"),

    INVEST_FEE("利息管理费"),

    TRANSFER_FEE("债权转让手续费"),

    ADMIN_INTERVENTION("管理员干预"),

    REFERRER_REWARD("推荐人奖励"),

    LOAN_REMAINING_INTEREST("标的剩余利息"),

    COUPON("体验券"),

    COUPON_RED_ENVELOPE("红包"),

    LOTTERY_CASH("抽奖现金"),

    EXTRA_RATE("投资奖励"),

    EXPERIENCE_INTEREST("体验金项目回款"),

    INVEST_CASH_BACK("现金补贴"),

    MEMBERSHIP_PRIVILEGE_PURCHASE("购买增值特权");

    private final String description;

    SystemBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
