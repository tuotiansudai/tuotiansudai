package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanDetailsDto;

public class LoanDetailsModel {
    private long id;
    private long loanId;
    private String declaration;

    public LoanDetailsModel() {
    }

    public LoanDetailsModel(long loanId, String declaration) {
        this.loanId = loanId;
        this.declaration = declaration;
    }

    public LoanDetailsModel(LoanDetailsDto loanDetailsDto) {
        this.loanId = loanDetailsDto.getLoanId();
        this.declaration = loanDetailsDto.getDeclaration();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
