package com.tuotiansudai.enums;

public enum BankCallbackType {

    REGISTER("实名认证", "/register/account", "/register/account", "get", "tuotian://certificate"),
    CARD_BIND("银行卡绑定", "/bank-card/bind/source/WEB", "/bank-card/bind/source/M", "post", "tuotian://bindcard"),
    CANCEL_CARD_BIND("银行卡解绑", "/personal-info", "/personal-info", "get", "tuotian://unbindcard"),

    RECHARGE("充值", "/recharge", "/recharge", "get", "tuotian://recharge"),
    WITHDRAW("提现申请", "/withdraw", "/withdraw",  "get", "tuotian://withdraw"),

    AUTHORIZATION("开启免密投资", "/personal-info", "/personal-info", "get", "tuotian://sign"),

    PASSWORD_RESET("重置支付密码", "/personal-info/reset-bank-password/source/WEB",  "/personal-info/reset-bank-password/source/M", "post", "tuotian://resetPwd"),

    PHONE_UPDATE("修改", "/personal-info", "/personal-info", "get", ""),

    LOAN_INVEST("投资", "/loan-list", "/loan-list",  "get", "tuotian://invest"),
    LOAN_FAST_INVEST("投资", "/loan-list", "/loan-list",  "get", ""),
    LOAN_CREDIT_INVEST("投资", "/transfer-list", "/transfer-list",  "get", "tuotian://invest-transfer"),

    LOAN_REPAY("还款", "/loaner/loan-list", "/loaner/loan-list",  "get", ""),
    LOAN_FAST_REPAY("", "", "", "get", ""),

    MERCHANT_TRANSFER("", "", "", "get", ""),
    ;

    private final String title;

    private final String webRetryPath;

    private final String mRetryPath;

    private final String method;

    private final String mobileLink;

    BankCallbackType(String title, String webRetryPath, String mRetryPath, String method, String mobileLink) {
        this.title = title;
        this.webRetryPath = webRetryPath;
        this.mRetryPath = mRetryPath;
        this.method = method;
        this.mobileLink = mobileLink;
    }

    public String getTitle() {
        return title;
    }

    public String getWebRetryPath() {
        return webRetryPath;
    }

    public String getMethod() {
        return method;
    }

    public String getMRetryPath() {
        return mRetryPath;
    }

    public String getMobileLink() {
        return mobileLink;
    }
}
