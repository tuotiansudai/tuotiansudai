package com.tuotiansudai.enums;

public enum BankCallbackType {

    REGISTER("实名认证成功", "/register/account", "get"),
    CARD_BIND("银行卡绑定成功", "/bank-card/bind/source/WEB", "post"),
    CANCEL_CARD_BIND("银行卡解绑成功", "/personal-info", "get"),

    RECHARGE("充值成功", "/recharge", "get"),
    WITHDRAW("提现申请成功", "/withdraw", "get"),

    AUTHORIZATION("免密投资开启成功", "/personal-info", "get"),

    PASSWORD_RESET("支付密码重置成功", "/personal-info/reset-bank-password/source/WEB", "post"),


    PHONE_UPDATE("", "", "get"),

    LOAN_INVEST("投资成功", "/loan-list", "get"),
    LOAN_FAST_INVEST("投资成功", "/loan-list", "get"),
    LOAN_CREDIT_INVEST("投资成功", "/transfer-list", "get"),

    LOAN_REPAY("还款成功", "/loaner/loan-list", "get"),
    LOAN_FAST_REPAY("", "", "get"),

    MERCHANT_TRANSFER("", "", "get"),

    ;


    private final String title;

    private final String retryPath;

    private final String method;

    BankCallbackType(String title, String retryPath, String method) {
        this.title = title;
        this.retryPath = retryPath;
        this.method = method;
    }

    public String getTitle() {
        return title;
    }

    public String getRetryPath() {
        return retryPath;
    }

    public String getMethod() {
        return method;
    }
}
