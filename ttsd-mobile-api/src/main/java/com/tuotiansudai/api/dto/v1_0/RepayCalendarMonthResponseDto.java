package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RepayCalendarMonthResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "回款日集合", example = "[1,2,3]")
    private List<String> repayDate;

    @ApiModelProperty(value = "待收金额", example = "100")
    private String repayAmount;

    @ApiModelProperty(value = "待收收益", example = "10")
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
