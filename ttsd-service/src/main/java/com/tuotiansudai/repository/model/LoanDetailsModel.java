package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanDetailsDto;

import java.io.Serializable;

public class LoanDetailsModel implements Serializable {
    private long id;
    private long loanId;
    private String declaration;
    private String extraSource;
    private boolean isActivity;

    public LoanDetailsModel() {
    }

    public LoanDetailsModel(long loanId, String declaration, String extraSource, boolean isActivity) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
        this.isActivity = isActivity;
    }

    public LoanDetailsModel(LoanDetailsDto loanDetailsDto) {
        this.loanId = loanDetailsDto.getLoanId();
        this.declaration = loanDetailsDto.getDeclaration();
        this.extraSource = loanDetailsDto.getExtraSource();
        this.isActivity = loanDetailsDto.isActivity();
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

    public String getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(String extraSource) {
        this.extraSource = extraSource;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }
}
