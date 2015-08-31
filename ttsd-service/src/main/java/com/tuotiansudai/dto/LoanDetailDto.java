package com.tuotiansudai.dto;

public class LoanDetailDto extends BaseDataDto {
    private long loanAmount;

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }
}
