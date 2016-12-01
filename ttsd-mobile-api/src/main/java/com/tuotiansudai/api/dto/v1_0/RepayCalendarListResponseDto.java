package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RepayCalendarListResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "待收总额", example = "1000")
    private String totalRepayAmount;

    @ApiModelProperty(value = "待收收益", example = "100")
    private String totalExpectedRepayAmount;

    @ApiModelProperty(value = "待收记录", example = "list")
    List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtos;

    public String getTotalRepayAmount() {
        return totalRepayAmount;
    }

    public void setTotalRepayAmount(String totalRepayAmount) {
        this.totalRepayAmount = totalRepayAmount;
    }

    public String getTotalExpectedRepayAmount() {
        return totalExpectedRepayAmount;
    }

    public void setTotalExpectedRepayAmount(String totalExpectedRepayAmount) {
        this.totalExpectedRepayAmount = totalExpectedRepayAmount;
    }

    public List<RepayCalendarYearResponseDto> getRepayCalendarYearResponseDtos() {
        return repayCalendarYearResponseDtos;
    }

    public void setRepayCalendarYearResponseDtos(List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtos) {
        this.repayCalendarYearResponseDtos = repayCalendarYearResponseDtos;
    }
}
