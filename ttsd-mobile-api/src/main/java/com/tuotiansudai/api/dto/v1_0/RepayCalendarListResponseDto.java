package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class RepayCalendarListResponseDto extends BaseResponseDataDto{
    private String totalRepayAmount;
    private String totalExpectedRepayAmount;
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
