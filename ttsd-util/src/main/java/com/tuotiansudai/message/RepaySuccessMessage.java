package com.tuotiansudai.message;


import java.io.Serializable;

public class RepaySuccessMessage implements Serializable {

    private Long loanRepayId;

    private boolean isAdvanced;

    public RepaySuccessMessage(){}

    public RepaySuccessMessage(Long loanRepayId,boolean isAdvanced) {
        this.loanRepayId = loanRepayId;
        this.isAdvanced = isAdvanced;
    }

    public Long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(Long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public boolean isAdvanced() {
        return isAdvanced;
    }

    public void setAdvanced(boolean advanced) {
        isAdvanced = advanced;
    }
}
