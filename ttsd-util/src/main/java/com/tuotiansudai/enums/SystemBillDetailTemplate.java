package com.tuotiansudai.enums;

import java.text.MessageFormat;

public enum SystemBillDetailTemplate {

    BIND_BANK_CARD_DETAIL_TEMPLATE("用户({0})绑定银行({1})卡号({2})"),

    REPLACE_BANK_CARD_DETAIL_TEMPLATE("用户({0})更换银行({1})卡号({2})"),

    REFERRER_REWARD_DETAIL_TEMPLATE("推荐人({0})推荐用户({1})投资(investId={2})奖励"),

    INVEST_FEE_DETAIL_TEMPLATE("标的(loanId = {0})还款(loanRepayId={1})手续费"),

    COUPON_INTEREST_DETAIL_TEMPLATE("{0}(userCouponId={1}), 还款(couponRepayId={2}), 发放利息({3}分)"),

    COUPON_RED_ENVELOPE_DETAIL_TEMPLATE("{0}(userCouponId={1}), 放款(loanId={2}), 发放红包({3}分)"),

    PUBLIC_RECHARGE_DETAIL_TEMPLATE("标的代理人({0}), 企业充值({1}分 rechargeId={2})"),

    TRANSFER_FEE_DETAIL_TEMPLATE("债权转让人({0}), 转让债权(transferApplicationId={1}), 管理费({2})"),

    LOTTERY_CASH_DETAIL_TEMPLATE("用户{0}抽奖,抽到现金{1}分"),

    EXPERIENCE_INTEREST_DETAIL_TEMPLATE("发放用户({0})新手体检金收益{1}分"),

    EXTRA_RATE_DETAIL_TEMPLATE("用户({0})投资(investId={1})加息奖励"),

    INVEST_RETURN_CASH_DETAIL_TEMPLATE("用户({0})投资逢万返百标,获取现金奖励{1}分"),

    IPHONEX_ACTIVITY_CASH_DETAIL_TEMPLATE("用户({0})在iPhoneX活动中,获取现金奖励{1}分"),

    YEAR_END_AWARDS_CASH_DETAIL_TEMPLATE("用户({0})在年终奖活动中,获取现金奖励{1}分"),

    CASH_SNOWBALL_CASH_DETAIL_TEMPLATE("用户({0})在现金滚雪球中,获取现金奖励{1}分"),

    PAYROLL_DETAIL_TEMPLATE("为用户({0})代发工资"),

    CASH_START_WORK_DETAIL_TEMPLATE("用户({0})在惊喜不重样加息不打烊活动中,获取现金奖励{1}分"),

    LOAN_OUT_SEND_CASH_REWARD_DETAIL_TEMPLATE("用户({0})在标的放款中,获取现金奖励{1}分"),
    ;

    private final String template;

    SystemBillDetailTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public String buildDetail(Object... arguments){
        return MessageFormat.format(template, arguments);
    }
}
