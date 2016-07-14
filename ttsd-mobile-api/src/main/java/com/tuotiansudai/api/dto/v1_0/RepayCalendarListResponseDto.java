package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class RepayCalendarListResponseDto extends BaseResponseDataDto{
    List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtos;

    public List<RepayCalendarYearResponseDto> getRepayCalendarYearResponseDtos() {
        return repayCalendarYearResponseDtos;
    }

    public void setRepayCalendarYearResponseDtos(List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtos) {
        this.repayCalendarYearResponseDtos = repayCalendarYearResponseDtos;
    }
}
