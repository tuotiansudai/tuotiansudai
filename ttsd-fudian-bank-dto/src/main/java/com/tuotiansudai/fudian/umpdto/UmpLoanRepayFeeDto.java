package com.tuotiansudai.fudian.umpdto;


public class UmpLoanRepayFeeDto extends UmpBaseDto{

    private long loanId;

    private long loanRepayId;

    private long amount;

    private boolean isAdvance;

    public UmpLoanRepayFeeDto() {
    }

    public UmpLoanRepayFeeDto(String loginName, String payUserId, long loanId, long loanRepayId, long amount, boolean isAdvance) {
        super(loginName, payUserId);
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.amount = amount;
        this.isAdvance = isAdvance;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getAmount() {
        return amount;
    }

    public boolean getIsAdvance() {
        return isAdvance;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && loanRepayId > 0
                && amount > 0;
    }

}
