package com.tuotiansudai.api.dto;

public class LoanDetailRequestDto extends BaseParamDto{
    private String loanId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
