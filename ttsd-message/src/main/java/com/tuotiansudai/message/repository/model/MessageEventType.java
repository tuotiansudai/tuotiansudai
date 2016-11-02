package com.tuotiansudai.message.repository.model;

public enum MessageEventType {
    REGISTER_USER_SUCCESS("注册成功"),
    REGISTER_ACCOUNT_SUCCESS("实名认证成功"),
    WITHDRAW_APPLICATION_SUCCESS("提现申请成功"),
    WITHDRAW_SUCCESS("提现到账"),
    RECOMMEND_SUCCESS("推荐成功"),
    RECOMMEND_AWARD_SUCCESS("推荐获得奖励"),
    INVEST_SUCCESS("投资成功"),
    LOAN_OUT_SUCCESS("放款成功"),
    REPAY_SUCCESS("正常回款"),
    ADVANCED_REPAY("提前还款"),
    TRANSFER_SUCCESS("债权转让成功"),
    TRANSFER_FAIL("债权转让失败(到期取消)"),
    MEMBERSHIP_EXPIRED("会员到期提醒"),
    MEMBERSHIP_UPGRADE("会员升级"),
    COUPON_5DAYS_EXPIRED_ALERT("优惠券到期提醒(5天后)"),
    BIRTHDAY("生日提醒"),
    MEMBERSHIP_BUY_SUCCESS("会员购买成功");

    private final String description;

    MessageEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
