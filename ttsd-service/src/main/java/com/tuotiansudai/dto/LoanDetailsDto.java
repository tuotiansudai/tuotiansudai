package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.util.List;

public class LoanDetailsDto {
    private long loanId;
    private String declaration;
    private List<Source> extraSource;
    private boolean activity;
    private String activityDesc;

    public LoanDetailsDto() {
    }

    public LoanDetailsDto(long loanId, String declaration, List<Source> extraSource, boolean activity, String activityDesc) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
        this.activity = activity;
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

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
}
