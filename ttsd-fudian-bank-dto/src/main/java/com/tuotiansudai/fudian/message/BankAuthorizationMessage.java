package com.tuotiansudai.fudian.message;


public class BankAuthorizationMessage {

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankAuthorizationMessage() {
    }

    public BankAuthorizationMessage(String loginName, String mobile, String bankUserName, String bankAccountNo, String bankOrderNo, String bankOrderDate) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
