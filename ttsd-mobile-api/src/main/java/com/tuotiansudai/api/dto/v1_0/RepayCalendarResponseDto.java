package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.InvestRepayModel;

import java.text.SimpleDateFormat;

public class RepayCalendarResponseDto extends BaseResponseDataDto{

    private String month;
    private String repayDate;
    private String repayAmount;
    private String expectedRepayAmount;

    public RepayCalendarResponseDto(InvestRepayModel investRepayModel,SimpleDateFormat simpleDateFormat){
        if(simpleDateFormat.toPattern().equals("MM")){
            this.month = simpleDateFormat.format(investRepayModel.getRepayDate());
        }else if(simpleDateFormat.toPattern().equals("dd")){
            this.repayDate = simpleDateFormat.format(investRepayModel.getRepayDate());
        }
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

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }
}
