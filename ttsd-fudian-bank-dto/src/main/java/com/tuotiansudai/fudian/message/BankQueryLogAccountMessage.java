package com.tuotiansudai.fudian.message;

import java.util.List;

public class BankQueryLogAccountMessage extends BankBaseMessage {

    private String bankUserName;

    private String bankAccountNo;

    private String bankOrderDateStart;

    private String bankOrderDateEnd;

    private List<BankQueryLogAccountItemMessage> items;

    public BankQueryLogAccountMessage() {
    }

    public BankQueryLogAccountMessage(boolean status, String message) {
        super(status, message);
    }

    public BankQueryLogAccountMessage(String bankUserName, String bankAccountNo, String bankOrderDateStart, String bankOrderDateEnd, List<BankQueryLogAccountItemMessage> items) {
        super(true, null);
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankOrderDateStart = bankOrderDateStart;
        this.bankOrderDateEnd = bankOrderDateEnd;
        this.items = items;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public String getBankOrderDateStart() {
        return bankOrderDateStart;
    }

    public String getBankOrderDateEnd() {
        return bankOrderDateEnd;
    }

    public List<BankQueryLogAccountItemMessage> getItems() {
        return items;
    }
}