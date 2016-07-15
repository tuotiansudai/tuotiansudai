package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanListsDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MobileAppBookingLoanServiceTest extends ServiceTestBase{

    @Autowired
    private MobileAppBookingLoanService mobileAppBookingLoanService;

    @Test
    public void shouldGetBookingLoanIsOk(){
        BaseResponseDto<BookingLoanListsDto> bookingLoan = mobileAppBookingLoanService.getBookingLoan();
        assertThat(bookingLoan.getData().getBookingLoanDtoList().size(),is(3));
    }
}
