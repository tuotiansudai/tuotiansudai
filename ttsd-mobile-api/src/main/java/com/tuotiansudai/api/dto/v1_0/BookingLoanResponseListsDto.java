package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class BookingLoanResponseListsDto extends BaseResponseDataDto{
    private List<BookingLoanResponseDto> bookingLoans;

    public List<BookingLoanResponseDto> getBookingLoans() {
        return bookingLoans;
    }

    public void setBookingLoans(List<BookingLoanResponseDto> bookingLoans) {
        this.bookingLoans = bookingLoans;
    }
}
