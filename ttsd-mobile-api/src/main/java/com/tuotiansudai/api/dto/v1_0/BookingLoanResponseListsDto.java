package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class BookingLoanResponseListsDto extends BaseResponseDataDto{
    private List<BookingLoanResponseDto> bookingLoanResponseDtoList;

    public List<BookingLoanResponseDto> getBookingLoanResponseDtoList() {
        return bookingLoanResponseDtoList;
    }

    public void setBookingLoanResponseDtoList(List<BookingLoanResponseDto> bookingLoanResponseDtoList) {
        this.bookingLoanResponseDtoList = bookingLoanResponseDtoList;
    }
}
