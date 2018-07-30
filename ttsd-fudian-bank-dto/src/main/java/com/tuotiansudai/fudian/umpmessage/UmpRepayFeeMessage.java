package com.tuotiansudai.fudian.umpmessage;

public class UmpRepayFeeMessage {

    private long loanId;

    private long loanRepayId;

    private long fee;

    public UmpRepayFeeMessage() {
    }

    public UmpRepayFeeMessage(long loanId, long loanRepayId, long fee) {
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.fee = fee;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getFee() {
        return fee;
    }
}
