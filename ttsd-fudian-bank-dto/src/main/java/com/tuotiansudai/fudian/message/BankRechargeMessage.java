package com.tuotiansudai.fudian.message;


public class BankRechargeMessage extends BankBaseMessage{

    private long rechargeId;

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private long amount;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankRechargeMessage() {
    }

    public BankRechargeMessage(long rechargeId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, String bankOrderNo, String bankOrderDate) {
        this.rechargeId = rechargeId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.amount = amount;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public long getRechargeId() {
        return rechargeId;
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

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
