package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class BookingLoanListsDto extends BaseResponseDataDto{
    private List<BookingLoanDto> bookingLoanDtoList;

    public List<BookingLoanDto> getBookingLoanDtoList() {
        return bookingLoanDtoList;
    }

    public void setBookingLoanDtoList(List<BookingLoanDto> bookingLoanDtoList) {
        this.bookingLoanDtoList = bookingLoanDtoList;
    }
}
