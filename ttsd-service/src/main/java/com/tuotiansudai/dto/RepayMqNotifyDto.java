package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepayMqNotifyDto {

    private long loanRepayId;

    private boolean isAdvanced;

    public RepayMqNotifyDto(long loanRepayId, boolean isAdvanced) {
        this.loanRepayId = loanRepayId;
        this.isAdvanced = isAdvanced;
    }

    @JsonProperty(value = "isAdvanced")
    public boolean isAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(boolean isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }
}
