package com.tuotiansudai.message;


public class LoanOutSuccessMessage {

    private long loanId;

    public LoanOutSuccessMessage(){}

    public LoanOutSuccessMessage(long loanId) {
        this.loanId = loanId;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }
}
