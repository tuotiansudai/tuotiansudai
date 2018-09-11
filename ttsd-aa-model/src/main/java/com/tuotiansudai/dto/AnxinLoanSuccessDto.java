package com.tuotiansudai.dto;

import java.io.Serializable;

public class AnxinLoanSuccessDto implements Serializable {

    private long loanId;
    private String fullTime;

    public AnxinLoanSuccessDto() {
    }

    public AnxinLoanSuccessDto(long loanId, String fullTime) {
        this.loanId = loanId;
        this.fullTime = fullTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getFullTime() {
        return fullTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }
}
