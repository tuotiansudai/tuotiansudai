package com.tuotiansudai.fudian.message;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class BankQueryLoanMessage extends BankBaseMessage {

    private String loanTxNo;

    private String loanAccNo;

    private long amount;

    private long balance;

    private String loanStatus;

    public BankQueryLoanMessage() {
    }

    public BankQueryLoanMessage(boolean status, String message) {
        super(status, message);
    }

    public BankQueryLoanMessage(String loanTxNo, String loanAccNo, long amount, long balance, String loanStatus) {
        super(true, null);
        this.loanTxNo = loanTxNo;
        this.loanAccNo = loanAccNo;
        this.amount = amount;
        this.balance = balance;
        this.loanStatus = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("0", "发标成功（可以进行投资）")
                .put("1", "投标中（有一笔或者多笔投资）")
                .put("2", "满标放款成功")
                .put("3", "还款中（有一笔或者多笔还款）")
                .put("4", "还款完成")
                .put("5", "撤标")
                .build()).get(loanStatus);
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public String getLoanAccNo() {
        return loanAccNo;
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
