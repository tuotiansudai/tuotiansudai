package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.InvestRepayModel;

import java.text.SimpleDateFormat;

public class RepayCalendarYearResponseDto extends BaseResponseDataDto{

    private String month;
    private String repayAmount;
    private String expectedRepayAmount;

    public RepayCalendarYearResponseDto(String month){
        this.month = month;
    }

    public RepayCalendarYearResponseDto(String month, String repayAmount, String expectedRepayAmount) {
        this.month = month;
        this.repayAmount = repayAmount;
        this.expectedRepayAmount = expectedRepayAmount;
    }

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
