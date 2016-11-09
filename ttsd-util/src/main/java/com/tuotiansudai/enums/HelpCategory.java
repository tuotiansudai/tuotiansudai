package com.tuotiansudai.enums;

public enum HelpCategory {
    KNOW_TTSD("认识拓天速贷"),
    SECURITY("安全保障"),
    REGISTER_REAL_NAME("注册登录与实名"),
    ACCOUNT_BANK_CARD("账户与银行卡"),
    RECHARGE_WITHDRAW("充值与提现"),
    INVEST_REPAY("投资与回款"),
    TRANSFER_SERVICE("债权转让服务"),
    RELIEVED_SIGN("安心签"),
    COUPON_REFERRER("优惠券与好友推荐"),
    POINT_MEMBERSHIP("积分与会员");

    private final String description;

    HelpCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
