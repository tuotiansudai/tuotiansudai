package com.tuotiansudai.fudian.message;

import java.util.List;

public class BankQueryLogLoanAccountMessage extends BankBaseMessage {

    private String loanTxNo;

    private String loanAccNo;

    private List<BankQueryLogLoanAccountItemMessage> items;

    public BankQueryLogLoanAccountMessage() {
    }

    public BankQueryLogLoanAccountMessage(boolean status, String message) {
        super(status, message);
    }

    public BankQueryLogLoanAccountMessage(String loanTxNo, String loanAccNo, List<BankQueryLogLoanAccountItemMessage> items) {
        super(true, null);
        this.loanTxNo = loanTxNo;
        this.loanAccNo = loanAccNo;
        this.items = items;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public String getLoanAccNo() {
        return loanAccNo;
    }

    public List<BankQueryLogLoanAccountItemMessage> getItems() {
        return items;
    }
}