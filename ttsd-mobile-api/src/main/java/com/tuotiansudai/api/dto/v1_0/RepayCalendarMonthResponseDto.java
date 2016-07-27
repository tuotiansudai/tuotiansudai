package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class RepayCalendarMonthResponseDto extends BaseResponseDataDto {

    private List<String> repayDate;
    private String repayAmount;
    private String expectedRepayAmount;

    public List<String> getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(List<String> repayDate) {
        this.repayDate = repayDate;
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
