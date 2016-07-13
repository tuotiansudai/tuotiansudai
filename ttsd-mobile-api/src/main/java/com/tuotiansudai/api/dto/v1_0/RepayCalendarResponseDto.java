package com.tuotiansudai.api.dto.v1_0;


public class RepayCalendarResponseDto extends BaseResponseDataDto{

    private String month;
    private String repayAmount;
    private String expectedRepayAmount;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
}
