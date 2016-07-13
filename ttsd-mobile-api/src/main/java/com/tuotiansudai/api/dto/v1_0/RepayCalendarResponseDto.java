package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.InvestRepayModel;

public class RepayCalendarResponseDto extends BaseResponseDataDto{

    private String month;
    private String repayAmount;
    private String expectedRepayAmount;

    public RepayCalendarResponseDto(InvestRepayModel investRepayModel,String month){
        this.month = month;
        this.repayAmount = String.valueOf(investRepayModel.getActualInterest() - investRepayModel.getActualFee());
        this.expectedRepayAmount = String.valueOf(investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee());
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
