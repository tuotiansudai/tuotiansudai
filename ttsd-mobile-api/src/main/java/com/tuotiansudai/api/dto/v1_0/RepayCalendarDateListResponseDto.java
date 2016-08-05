package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class RepayCalendarDateListResponseDto extends BaseResponseDataDto {

    private String totalAmount;

    private List<RepayCalendarDateResponseDto> repayCalendarDateResponseDtoList;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<RepayCalendarDateResponseDto> getRepayCalendarDateResponseDtoList() {
        return repayCalendarDateResponseDtoList;
    }

    public void setRepayCalendarDateResponseDtoList(List<RepayCalendarDateResponseDto> repayCalendarDateResponseDtoList) {
        this.repayCalendarDateResponseDtoList = repayCalendarDateResponseDtoList;
    }
}
