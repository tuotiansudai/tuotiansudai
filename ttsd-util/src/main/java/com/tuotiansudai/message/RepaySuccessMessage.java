package com.tuotiansudai.message;


import java.io.Serializable;

public class RepaySuccessMessage implements Serializable {

    private Long loanRepayId;

    private boolean advanced;

    public RepaySuccessMessage(){}

    public RepaySuccessMessage(Long loanRepayId,boolean advanced) {
        this.loanRepayId = loanRepayId;
        this.advanced = advanced;
    }

    public Long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(Long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }
}
