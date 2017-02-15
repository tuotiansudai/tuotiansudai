package com.tuotiansudai.message;


import java.io.Serializable;

public class LoanOutSuccessMessage implements Serializable {

    private Long loanId;

    public LoanOutSuccessMessage(){}

    public LoanOutSuccessMessage(Long loanId) {
        this.loanId = loanId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}
