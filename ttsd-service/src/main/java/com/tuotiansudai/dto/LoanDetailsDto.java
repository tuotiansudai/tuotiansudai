package com.tuotiansudai.dto;

public class LoanDetailsDto {
    private long loanId;
    private String declaration;
    private String extraSource;
    private String activityDesc;

    public LoanDetailsDto() {
    }

    public LoanDetailsDto(long loanId, String declaration, String extraSource, String activityDesc) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
        this.activityDesc = activityDesc;
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

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
}
