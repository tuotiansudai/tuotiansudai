package com.tuotiansudai.fudian.message;

public class BankBindCardMessage extends BankBaseMessage {

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private String bank;

    private String bankCode;

    private String cardNumber;

    private String bankOrderNo;

    private String bankOrderDate;

    private boolean isInvestor;

    public BankBindCardMessage() {
    }

    public BankBindCardMessage(String loginName, String mobile, String bankUserName, String bankAccountNo, String bankOrderNo, String bankOrderDate, boolean isInvestor) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.isInvestor = isInvestor;
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

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public boolean isInvestor() {
        return isInvestor;
    }
}
