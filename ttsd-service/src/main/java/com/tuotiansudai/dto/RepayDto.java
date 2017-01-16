package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RepayDto {

    private long loanId;

    private long loanRepayId;

    private boolean isAdvanced;

    public RepayDto(){}

    public RepayDto(long loanRepayId, boolean isAdvanced) {
        this.loanRepayId = loanRepayId;
        this.isAdvanced = isAdvanced;
    }

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

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }
}
