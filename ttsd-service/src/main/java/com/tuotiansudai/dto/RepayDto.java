package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RepayDto {

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private long loanId;

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

}
