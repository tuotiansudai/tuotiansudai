package com.tuotiansudai.enums;

public enum UmpCallbackType {

    PTP_MER_BIND_CARD("银行卡绑定"),
    PTP_MER_REPLACE_CARD("银行卡换卡申请"),
    MER_RECHARGE_PERSON("充值"),
    CUST_WITHDRAWALS("提现申请"),
    ;

    private final String title;

    UmpCallbackType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
