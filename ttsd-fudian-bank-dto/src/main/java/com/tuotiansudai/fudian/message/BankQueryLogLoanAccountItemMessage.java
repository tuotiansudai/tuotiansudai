package com.tuotiansudai.fudian.message;

public class BankQueryLogLoanAccountItemMessage {

    private String amount;

    private String balance;

    private String freezeBalance;

    private String tradeTime;

    private String bankOrderNo;

    private String bankOrderDate;

    private String remark;

    private String toBankUserName;

    public BankQueryLogLoanAccountItemMessage(String amount, String balance, String freezeBalance, String tradeTime, String bankOrderNo, String bankOrderDate, String remark, String toBankUserName) {
        this.amount = amount;
        this.balance = balance;
        this.freezeBalance = freezeBalance;
        this.tradeTime = tradeTime;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.remark = remark;
        this.toBankUserName = toBankUserName;
    }

    public String getAmount() {
        return amount;
    }

    public String getBalance() {
        return balance;
    }

    public String getFreezeBalance() {
        return freezeBalance;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public String getRemark() {
        return remark;
    }

    public String getToBankUserName() {
        return toBankUserName;
    }
}