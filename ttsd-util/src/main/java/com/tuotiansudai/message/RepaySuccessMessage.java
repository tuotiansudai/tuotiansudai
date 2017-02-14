package com.tuotiansudai.message;


import java.io.Serializable;

public class RepaySuccessMessage implements Serializable {

    private Long loanRepayId;

    public RepaySuccessMessage(){}

    public RepaySuccessMessage(Long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public Long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(Long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }
}
