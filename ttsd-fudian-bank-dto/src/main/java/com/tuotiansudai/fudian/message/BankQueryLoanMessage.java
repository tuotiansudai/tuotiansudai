package com.tuotiansudai.fudian.message;

public class BankQueryLoanMessage extends BankBaseMessage {

    private long amount;

    private long balance;

    private String loanStatus;

    public BankQueryLoanMessage() {
    }

    public BankQueryLoanMessage(long amount, long balance, String loanStatus, boolean status, String message) {
        super(status, message);
        this.amount = amount;
        this.balance = balance;
        this.loanStatus = loanStatus;
    }

    public long getAmount() {
        return amount;
    }

    public long getBalance() {
        return balance;
    }

    public String getLoanStatus() {
        return loanStatus;
    }
}
