package com.tuotiansudai.repository.model;

public enum SystemBillDetailTemplate {

    BIND_BANK_CARD_DETAIL_TEMPLATE("用户({0})绑定银行({1})卡号({2})"),

    REPLACE_BANK_CARD_DETAIL_TEMPLATE("用户({0})更换银行({1})卡号({2})"),

    REFERRER_REWARD_DETAIL_TEMPLATE("推荐人({0})推荐用户({1})投资(investId={2})奖励"),

    INVEST_FEE_DETAIL_TEMPLATE("投资人({0})投资返款(investRepayId={1})手续费"),

    LOAN_REMAINING_INTEREST_DETAIL_TEMPLATE("标的(loanRepayId={0})还款完成, 剩余利息({1}分)"),

    COUPON_INTEREST_DETAIL_TEMPLATE("{0}体验券(userCouponId={1}), 还款(loanRepayId={2}), 发放利息({3}分)");

    private final String template;

    SystemBillDetailTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
