package com.tuotiansudai.enums;

public enum UmpCallbackType {

    PTP_MER_BIND_CARD("银行卡绑定", ""),
    PTP_MER_REPLACE_CARD("银行卡换卡", ""),
    MER_RECHARGE_PERSON("充值", ""),
    CUST_WITHDRAWALS("提现申请", ""),
    ;

    private final String title;

    private final String webRetryPath;

    UmpCallbackType(String title, String webRetryPath) {
        this.title = title;
        this.webRetryPath = webRetryPath;
    }

    public String getTitle() {
        return title;
    }

    public String getWebRetryPath() {
        return webRetryPath;
    }
}
