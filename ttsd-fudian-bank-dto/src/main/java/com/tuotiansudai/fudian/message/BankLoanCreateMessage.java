package com.tuotiansudai.fudian.message;

public class BankLoanCreateMessage extends SyncMessage {

    private String loanName;

    private String loanTxNo;

    private String loanAccNo;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankLoanCreateMessage(boolean status, String message) {
        super(status, message);
    }

    public BankLoanCreateMessage(String loanName, String loanTxNo, String loanAccNo, String bankOrderNo, String bankOrderDate) {
        super(true, null);
        this.loanName = loanName;
        this.loanTxNo = loanTxNo;
        this.loanAccNo = loanAccNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public String getLoanName() {
        return loanName;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public String getLoanAccNo() {
        return loanAccNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
