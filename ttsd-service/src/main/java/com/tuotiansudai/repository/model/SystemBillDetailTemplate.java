package com.tuotiansudai.repository.model;

public enum SystemBillDetailTemplate {

    BIND_BANK_CARD_DETAIL_TEMPLATE("用户({0})绑定银行({1})卡号({2})"),

    REPLACE_BANK_CARD_DETAIL_TEMPLATE("用户({0})更换银行({1})卡号({2})"),

    REFERRER_REWARD_DETAIL_TEMPLATE("推荐人({0})推荐用户({1})投资(investId={2})奖励"),

    INVEST_FEE_DETAIL_TEMPLATE("投资人({0})投资返款(investRepayId={1})手续费"),

    LOAN_REMAINING_INTEREST_DETAIL_TEMPLATE("标的(loanRepayId={0})还款完成, 剩余利息({1}分)"),

    COUPON_INTEREST_DETAIL_TEMPLATE("{0}(userCouponId={1}), 还款(loanRepayId={2}), 发放利息({3}分)"),

    COUPON_RED_ENVELOPE_DETAIL_TEMPLATE("{0}(userCouponId={1}), 放款(loanId={2}), 发放红包({3}分)"),

    PUBLIC_RECHARGE_DETAIL_TEMPLATE("标的代理人({0}), 企业充值({1}分 rechargeId={2})"),

    LOTTERY_CASH_DETAIL_TEMPLATE("用户{0}抽奖,抽到现金{1}分");

    private final String template;

    SystemBillDetailTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
