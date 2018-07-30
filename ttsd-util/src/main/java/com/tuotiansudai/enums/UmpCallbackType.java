package com.tuotiansudai.enums;

public enum UmpCallbackType {

    PTP_MER_BIND_CARD("银行卡绑定成功"),
    PTP_MER_REPLACE_CARD("银行卡换卡申请成功"),
    MER_RECHARGE_PERSON("充值成功"),
    CUST_WITHDRAWALS("提现申请成功"),
    REPAY_PROJECT_TRANSFER("还款成功"),
    ;

    private final String title;

    UmpCallbackType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
