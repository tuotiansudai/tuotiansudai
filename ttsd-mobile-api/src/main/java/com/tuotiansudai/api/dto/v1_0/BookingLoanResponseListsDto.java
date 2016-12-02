package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BookingLoanResponseListsDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的列表", example = "list")
    private List<BookingLoanResponseDto> bookingLoans;

    public List<BookingLoanResponseDto> getBookingLoans() {
        return bookingLoans;
    }

    public void setBookingLoans(List<BookingLoanResponseDto> bookingLoans) {
        this.bookingLoans = bookingLoans;
    }
}
