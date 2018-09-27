package com.tuotiansudai.enums;

public enum HelpCategory {
    KNOW_TTSD("know_ttsd", "认识拓天速贷", 1),
    SECURITY("security","安全保障", 2),
    REGISTER_REAL_NAME("register_real_name","注册登录与实名", 3),
    ACCOUNT_BANK_CARD("account_bank_card","账户与银行卡", 4),
    RECHARGE_WITHDRAW("recharge_withdraw","充值与提现", 5),
    INVEST_REPAY("invest_repay","出借与回款", 6),
    TRANSFER_SERVICE("transfer_service","债权转让服务", 7),
    RELIEVED_SIGN("relieved_sign","安心签", 8),
    COUPON_REFERRER("coupon_referrer","优惠券与好友推荐", 9),
    POINT_MEMBERSHIP("point_membership","积分与会员", 10);

    private String code;
    private String name;
    private int order;

    HelpCategory(String code, String name, int order) {
        this.code = code;
        this.name = name;
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static String getNameByCode(String code) {
        for (HelpCategory helpCategory : HelpCategory.values()) {
            if (helpCategory.code.equals(code)) {
                return helpCategory.getName();
            }
        }
        return null;
    }


}
