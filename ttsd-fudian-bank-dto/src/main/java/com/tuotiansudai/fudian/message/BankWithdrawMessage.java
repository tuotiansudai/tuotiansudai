package com.tuotiansudai.fudian.message;

public class BankWithdrawMessage extends BankBaseMessage {

    private long withdrawId;

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private long amount;

    private long fee;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankWithdrawMessage() {
    }

    public BankWithdrawMessage(long withdrawId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, long fee, String bankOrderNo, String bankOrderDate) {
        this.withdrawId = withdrawId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.amount = amount;
        this.fee = fee;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
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

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
