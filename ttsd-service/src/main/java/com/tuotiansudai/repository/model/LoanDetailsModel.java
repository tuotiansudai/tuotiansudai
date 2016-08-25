package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanDetailsDto;

import java.io.Serializable;
import java.util.List;

public class LoanDetailsModel implements Serializable {
    private long id;
    private long loanId;
    private String declaration;
    private List<Source> extraSource;

    public LoanDetailsModel() {
    }

    public LoanDetailsModel(long loanId, String declaration) {
        this.loanId = loanId;
        this.declaration = declaration;
    }

    public LoanDetailsModel(LoanDetailsDto loanDetailsDto) {
        this.loanId = loanDetailsDto.getLoanId();
        this.declaration = loanDetailsDto.getDeclaration();
        this.extraSource = loanDetailsDto.getExtraSource();
    }

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
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
