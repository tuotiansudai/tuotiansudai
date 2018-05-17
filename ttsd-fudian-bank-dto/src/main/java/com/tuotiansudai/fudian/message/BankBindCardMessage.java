package com.tuotiansudai.fudian.message;

import java.io.Serializable;

public class BankBindCardMessage implements Serializable {

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private String bank;

    private String bankCode;

    private String cardNumber;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankBindCardMessage() {
    }

    public BankBindCardMessage(String loginName, String mobile, String bankUserName, String bankAccountNo, String bank, String bankCode, String cardNumber, String bankOrderNo, String bankOrderDate) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bank = bank;
        this.bankCode = bankCode;
        this.cardNumber = cardNumber;
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

    public String getBank() {
        return bank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
