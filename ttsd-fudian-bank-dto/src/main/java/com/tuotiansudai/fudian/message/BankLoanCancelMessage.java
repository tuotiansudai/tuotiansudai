package com.tuotiansudai.fudian.message;

public class BankLoanCancelMessage extends BankBaseMessage {

    private long loanId;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankLoanCancelMessage() {
    }

    public BankLoanCancelMessage(boolean status, String message) {
        super(status, message);
    }

    public BankLoanCancelMessage(long loanId, String bankOrderNo, String bankOrderDate) {
        super(true, null);
        this.loanId = loanId;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
