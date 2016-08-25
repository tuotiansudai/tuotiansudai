package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.util.List;

public class LoanDetailsDto {
    private long loanId;
    private String declaration;
    private List<Source> extraSource;

    public LoanDetailsDto() {
    }

    public LoanDetailsDto(long loanId, String declaration, List<Source> extraSource) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
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

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
    }
}
