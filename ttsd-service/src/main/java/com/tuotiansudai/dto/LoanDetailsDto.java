package com.tuotiansudai.dto;

public class LoanDetailsDto {
    private long loanId;
    private String declaration;

    public LoanDetailsDto() {
    }

    public LoanDetailsDto(long loanId, String declaration) {
        this.loanId = loanId;
        this.declaration = declaration;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }
}
