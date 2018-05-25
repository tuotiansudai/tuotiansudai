package com.tuotiansudai.fudian.message;

public class BankWithdrawMessage extends BankBaseMessage {

    private long withdrawId;

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private long amount;

    private long fee;

    private String bankCode;

    private String cardNumber;

    private String bankName;

    private String bankOrderNo;

    private String bankOrderDate;

    private String openId;

    public BankWithdrawMessage() {
    }

    public BankWithdrawMessage(long withdrawId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, long fee, String bankOrderNo, String bankOrderDate, String openId) {
        this.withdrawId = withdrawId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.amount = amount;
        this.fee = fee;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.openId = openId;
    }

    public long getWithdrawId() {
        return withdrawId;
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

    public long getAmount() {
        return amount;
    }

    public long getFee() {
        return fee;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public String getOpenId() {
        return openId;
    }
}
