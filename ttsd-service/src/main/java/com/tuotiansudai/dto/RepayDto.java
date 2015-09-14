package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RepayDto {

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private long loanId;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private int period;

    private long curpos

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
