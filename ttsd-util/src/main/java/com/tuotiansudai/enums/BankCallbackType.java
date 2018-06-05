package com.tuotiansudai.enums;

public enum BankCallbackType {

    REGISTER("", ""),
    CARD_BIND("银行卡绑定成功", "/personal-info"),
    CANCEL_CARD_BIND("银行卡解绑成功", "/personal-info"),

    RECHARGE("", ""),
    WITHDRAW("提现申请成功", "/withdraw"),

    AUTHORIZATION("", ""),

    PASSWORD_RESET("", ""),


    PHONE_UPDATE("", ""),

    LOAN_INVEST("投资成功", "/loan-list"),
    LOAN_FAST_INVEST("投资成功", "/loan-list"),
    LOAN_CREDIT_INVEST("", ""),

    LOAN_REPAY("还款成功", "/loaner/loan-list"),
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
