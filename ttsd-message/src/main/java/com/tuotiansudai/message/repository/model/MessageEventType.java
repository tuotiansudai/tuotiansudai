package com.tuotiansudai.message.repository.model;

public enum MessageEventType {
    REGISTER_USER_SUCCESS("注册"),
    REGISTER_ACCOUNT_SUCCESS("认证"),
    RECHARGE_SUCCESS("充值成功"),
    WITHDRAW_SUCCESS("提现成功"),
    INVEST_SUCCESS("投资成功"),
    TRANSFER_SUCCESS("转让成功"),
    TRANSFER_FAIL("转让成功"),
    LOAN_OUT_SUCCESS("放款成功"),
    REPAY_SUCCESS("还款成功"),
    RECOMMEND_SUCCESS("推荐成功"),
    RECOMMEND_AWARD_SUCCESS("推荐奖励"),
    ASSIGN_COUPON_SUCCESS("获得优惠券"),
    COUPON_5DAYS_EXPIRED_ALERT("优惠券5天后过期提醒");

    private final String description;

    MessageEventType(String description) {
        this.description = description;
    }
}
