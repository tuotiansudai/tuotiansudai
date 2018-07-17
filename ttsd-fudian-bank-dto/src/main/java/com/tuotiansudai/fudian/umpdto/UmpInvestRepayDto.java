package com.tuotiansudai.fudian.umpdto;


public class UmpInvestRepayDto extends UmpBaseDto{

    private long loanId;

    private long investRepayId;

    private long amount;

    private boolean isAdvance;

    public UmpInvestRepayDto() {
    }

    public UmpInvestRepayDto(String loginName, String payUserId, long loanId, long investRepayId, long amount, boolean isAdvance) {
        super(loginName, payUserId);
        this.loanId = loanId;
        this.investRepayId = investRepayId;
        this.amount = amount;
        this.isAdvance = isAdvance;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isAdvance() {
        return isAdvance;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && investRepayId > 0
                && amount > 0;
    }

}
