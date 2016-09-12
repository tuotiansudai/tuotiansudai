package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.util.List;

public class LoanDetailsDto {
    private long loanId;
    private String declaration;
    private String extraSource;
    private boolean isActivity;

    public LoanDetailsDto() {
    }

    public LoanDetailsDto(long loanId, String declaration, String extraSource, boolean isActivity) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
        this.isActivity = isActivity;
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
