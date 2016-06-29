package com.tuotiansudai.api.dto.v1_0;

public class InvestRepayDataDto extends BaseResponseDataDto {

    private int period;
    private String repayDate;
    private String actualRepayDate;
    private String expectedInterest;
    private String actualInterest;
    private String status;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(String actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
