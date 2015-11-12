package com.tuotiansudai.repository.model;

public enum SystemBillDetailTemplate {

    BIND_CARD_DETAIL_TEMPLATE("用户({0})绑定银行({1})卡号({2})"),

    REFERRER_REWARD_DETAIL_TEMPLATE("推荐人({0})推荐用户({2})投资(investId={3})奖励"),

    INVEST_FEE_DETAIL_TEMPLATE("投资人({0})投资返款(investRepay={1})手续费");

    private final String template;

    SystemBillDetailTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
