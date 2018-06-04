package com.tuotiansudai.fudian.message;

public class BankLoanFullMessage extends BankBaseMessage {

    private long loanId;

    private String loanTxNo;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankLoanFullMessage() {
    }

    public BankLoanFullMessage(long loanId, String loanTxNo, String bankOrderNo, String bankOrderDate) {
        this.loanId = loanId;
        this.loanTxNo = loanTxNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
