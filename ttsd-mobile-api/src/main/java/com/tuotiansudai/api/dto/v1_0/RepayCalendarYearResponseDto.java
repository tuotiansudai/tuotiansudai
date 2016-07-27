package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.InvestRepayModel;

import java.text.SimpleDateFormat;

public class RepayCalendarYearResponseDto extends BaseResponseDataDto{

    private String month;
    private String repayAmount;
    private String expectedRepayAmount;

    public RepayCalendarYearResponseDto(String month, String repayAmount, String expectedRepayAmount) {
        this.month = month;
        this.repayAmount = repayAmount;
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public RepayCalendarYearResponseDto(String month,InvestRepayModel investRepayModel){
        this.month = month;
        if(investRepayModel.getActualInterest() > 0){
            this.repayAmount = String.valueOf(investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getDefaultInterest());
            this.expectedRepayAmount = "0";
        }else if(investRepayModel.getExpectedInterest() > 0){
            this.repayAmount = "0";
            this.expectedRepayAmount = String.valueOf(investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest());
        }
    }

    public RepayCalendarYearResponseDto(String month,CouponRepayModel couponRepayModel){
        this.month = month;
        if(couponRepayModel.getActualInterest() > 0){
            this.repayAmount = String.valueOf(couponRepayModel.getActualInterest() - couponRepayModel.getActualFee());
        }else if(couponRepayModel.getExpectedInterest() > 0){
            this.expectedRepayAmount = String.valueOf(couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee());
        }
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
