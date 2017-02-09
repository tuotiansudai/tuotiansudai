package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepayDto {

    private long loanId;

    private boolean isAdvanced;

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    @JsonProperty(value = "isAdvanced")
    public boolean isAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(boolean isAdvanced) {
        this.isAdvanced = isAdvanced;
    }
}
