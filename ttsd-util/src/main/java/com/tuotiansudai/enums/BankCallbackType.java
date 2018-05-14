package com.tuotiansudai.enums;

public enum BankCallbackType {

    REGISTER("", ""),
    CARD_BIND("银行卡绑定成功", "/personal-info"),
    CANCEL_CARD_BIND("银行卡解绑成功", "/personal-info"),

    RECHARGE("", ""),
    WITHDRAW("", ""),

    AUTHORIZATION("", ""),

    PASSWORD_RESET("", ""),


    PHONE_UPDATE("", ""),

    LOAN_INVEST("", ""),
    LOAN_FAST_INVEST("", ""),
    LOAN_CREDIT_INVEST("", ""),

    LOAN_REPAY("", ""),
    LOAN_FAST_REPAY("", ""),

    MERCHANT_TRANSFER("", ""),

    ;


    private final String title;

    private final String retryPath;

    BankCallbackType(String title, String retryPath) {
        this.title = title;
        this.retryPath = retryPath;
    }

    public String getTitle() {
        return title;
    }

    public String getRetryPath() {
        return retryPath;
    }
}
