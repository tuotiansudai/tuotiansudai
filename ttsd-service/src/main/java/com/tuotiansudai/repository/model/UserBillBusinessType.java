package com.tuotiansudai.repository.model;

public enum UserBillBusinessType {
    RECHARGE_SUCCESS("充值成功"),
    WITHDRAW_SUCCESS("提现成功"),
    WITHDRAW_FAIL("提现失败"),
    INVEST_SUCCESS("投资成功"),
    LOAN_SUCCESS("借款成功"),
    NORMAL_REPAY("正常还款"),
    ADVANCE_REPAY("提前还款"),
    OVERDUE_REPAY("逾期还款"),
    INVEST_FEE("利息管理费"),
    ACTIVITY_REWARD("活动奖励"),
    REFERRER_REWARD("推荐奖励"),
    APPLY_WITHDRAW("申请提现"),
    OVER_INVEST_PAYBACK("超投返款"),
    CANCEL_INVEST_PAYBACK("流标返款"),
    ADMIN_INTERVENTION("管理员干预"),
    NEWBIE_COUPON("体验券");

    private final String description;

    UserBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
