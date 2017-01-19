package com.tuotiansudai.message;


public class RepaySuccessMessage {

    private Long loanRepayId;

    private boolean isAdvance;

    public RepaySuccessMessage(Long loanRepayId, boolean isAdvance) {
        this.loanRepayId = loanRepayId;
        this.isAdvance = isAdvance;
    }

    public Long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(Long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public boolean isAdvance() {
        return isAdvance;
    }

    public void setIsAdvance(boolean isAdvance) {
        this.isAdvance = isAdvance;
    }
}
