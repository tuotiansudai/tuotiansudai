package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class RepayCalendarListResponseDto extends BaseResponseDataDto{
    List<RepayCalendarResponseDto> repayCalendarResponseDtos;

    public List<RepayCalendarResponseDto> getRepayCalendarResponseDtos() {
        return repayCalendarResponseDtos;
    }

    public void setRepayCalendarResponseDtos(List<RepayCalendarResponseDto> repayCalendarResponseDtos) {
        this.repayCalendarResponseDtos = repayCalendarResponseDtos;
    }
}
