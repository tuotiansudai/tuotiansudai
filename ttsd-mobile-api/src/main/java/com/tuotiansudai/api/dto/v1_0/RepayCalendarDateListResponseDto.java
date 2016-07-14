package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class RepayCalendarDateListResponseDto extends BaseResponseDataDto{

    private List<RepayCalendarDateResponseDto> repayCalendarDateResponseDtoList;

    public List<RepayCalendarDateResponseDto> getRepayCalendarDateResponseDtoList() {
        return repayCalendarDateResponseDtoList;
    }

    public void setRepayCalendarDateResponseDtoList(List<RepayCalendarDateResponseDto> repayCalendarDateResponseDtoList) {
        this.repayCalendarDateResponseDtoList = repayCalendarDateResponseDtoList;
    }
}
