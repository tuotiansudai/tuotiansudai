package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

public class RepayCalendarYearResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "年", example = "2016")
    private String year;

    @ApiModelProperty(value = "月", example = "12")
    private String month;

    @ApiModelProperty(value = "待收金额", example = "100")
    private String repayAmount;

    @ApiModelProperty(value = "待收收益", example = "10")
    private String expectedRepayAmount;

    public RepayCalendarYearResponseDto(String year, String month, String repayAmount, String expectedRepayAmount) {
        this.year = year;
        this.month = month;
        this.repayAmount = repayAmount;
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public RepayCalendarYearResponseDto(String month, InvestRepayModel investRepayModel, Map<String, Long> investExtraRateAmount) {
        this.month = month;
        if (investRepayModel.getActualRepayDate() != null) {
            this.repayAmount = String.valueOf(investRepayModel.getRepayAmount() + investExtraRateAmount.get("repayAmount"));
            this.expectedRepayAmount = "0";
        } else {
            this.repayAmount = "0";
            this.expectedRepayAmount = String.valueOf(investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest() + investExtraRateAmount.get("expectedRepayAmount"));
        }
    }

    public RepayCalendarYearResponseDto(String month, CouponRepayModel couponRepayModel) {
        this.month = month;
        if (couponRepayModel.getActualRepayDate() != null) {
            this.repayAmount = String.valueOf(couponRepayModel.getRepayAmount());
        } else if (couponRepayModel.getExpectedInterest() > 0) {
            this.expectedRepayAmount = String.valueOf(couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee());
        }
    }

    public RepayCalendarYearResponseDto(String month, long repayMount) {
        this.month = month;
        this.repayAmount = String.valueOf(repayMount);
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
