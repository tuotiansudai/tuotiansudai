package com.tuotiansudai.api.dto.v1_0;


public class RepayCalendarDateResponseDto extends BaseResponseDataDto{

    private String loanName;
    private String repayAmount;
    private String expectedRepayAmount;
    private String period;
    private String periods;
    private String status;

    public RepayCalendarDateResponseDto() {
    }

    public RepayCalendarDateResponseDto(String loanName, String repayAmount, String expectedRepayAmount, String period, String periods, String status) {
        this.loanName = loanName;
        this.repayAmount = repayAmount;
        this.expectedRepayAmount = expectedRepayAmount;
        this.period = period;
        this.periods = periods;
        this.status = status;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public void setExpectedRepayAmount(String expectedRepayAmount) {
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
