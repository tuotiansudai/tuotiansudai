package com.tuotiansudai.repository.model;

import com.google.common.base.Joiner;
import com.tuotiansudai.dto.LoanCreateDetailsRequestDto;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;

public class LoanDetailsModel implements Serializable {
    private long id;
    private long loanId;
    private String declaration;
    private String extraSource;
    private boolean activity;
    private String activityDesc;

    public LoanDetailsModel() {
    }

    public LoanDetailsModel(long loanId, String declaration, String extraSource, boolean activity, String activityDesc) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
        this.activity = activity;
        this.activityDesc = activityDesc;
    }

    public LoanDetailsModel(long loanId, LoanCreateDetailsRequestDto loanDetails) {
        this.loanId = loanId;
        this.declaration = loanDetails.getDeclaration();
        this.extraSource = CollectionUtils.isNotEmpty(loanDetails.getExtraSource()) ? Joiner.on(",").join(loanDetails.getExtraSource()) : null;
        this.activity = loanDetails.isActivity();
        this.activityDesc = loanDetails.isActivity() ? loanDetails.getActivityDesc() : null;
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
