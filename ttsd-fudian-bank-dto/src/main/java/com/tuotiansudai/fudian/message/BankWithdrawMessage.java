package com.tuotiansudai.fudian.message;

import java.io.Serializable;

public class BankWithdrawMessage implements Serializable {

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

    private boolean status;

    public BankWithdrawMessage() {
    }

    public BankWithdrawMessage(long withdrawId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, long fee, String bankCode, String cardNumber, String bankName, String bankOrderNo, String bankOrderDate, String openId, boolean status) {
        this.withdrawId = withdrawId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.amount = amount;
        this.fee = fee;
        this.bankCode = bankCode;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.openId = openId;
        this.status = status;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public String getBankName() {
        return bankName;
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

    public boolean isStatus() {
        return status;
    }
}
