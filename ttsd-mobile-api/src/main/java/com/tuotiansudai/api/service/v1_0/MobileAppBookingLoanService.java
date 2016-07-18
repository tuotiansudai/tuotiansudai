package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanResponseListsDto;

public interface MobileAppBookingLoanService {
    BaseResponseDto<BookingLoanResponseListsDto> getBookingLoan();

    BaseResponseDto bookingLoan(BookingLoanRequestDto bookingLoanRequestDto);

}
