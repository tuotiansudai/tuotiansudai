package com.tuotiansudai.enums;

public enum PushType {
    REGISTER_USER_SUCCESS("AUTO", "注册成功"),
    REGISTER_ACCOUNT_SUCCESS("AUTO", "实名认证成功"),
    WITHDRAW_APPLICATION_SUCCESS("AUTO", "提现申请成功"),
    WITHDRAW_SUCCESS("AUTO", "提现到账"),
    RECOMMEND_SUCCESS("AUTO", "推荐成功"),
    RECOMMEND_AWARD_SUCCESS("AUTO", "推荐获得奖励"),
    INVEST_SUCCESS("AUTO", "出借成功"),
    LOAN_OUT_SUCCESS("AUTO", "放款成功"),
    REPAY_SUCCESS("AUTO", "正常回款"),
    ADVANCED_REPAY("AUTO", "提前还款"),
    TRANSFER_SUCCESS("AUTO", "债权转让成功"),
    TRANSFER_FAIL("AUTO", "债权转让失败(到期取消)"),
    MEMBERSHIP_PRIVILEGE_EXPIRED("AUTO", "增值特权到期提醒"),
    MEMBERSHIP_UPGRADE("AUTO", "会员升级"),
    COUPON_5DAYS_EXPIRED_ALERT("AUTO", "优惠券到期提醒(5天后)"),
    BIRTHDAY("AUTO", "生日提醒"),
    MEMBERSHIP_PRIVILEGE_BUY_SUCCESS("AUTO", "增值特权购买成功"),
    ASSIGN_COUPON_SUCCESS("AUTO", "优惠券发放成功"),

    PREHEAT("MANUAL", "预热标的"),
    PRESENT_SEND("MANUAL", "礼品派送"),
    IMPORTANT_EVENT("MANUAL", "重大事件"),
    HUMANISTIC_CARE("MANUAL", "人文关怀");

    private final String type;

    private final String description;

    PushType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
