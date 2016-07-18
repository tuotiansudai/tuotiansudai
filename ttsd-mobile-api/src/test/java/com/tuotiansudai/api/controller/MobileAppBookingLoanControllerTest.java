package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.controller.v1_0.MobileAppBookingLoanController;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppBookingLoanControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppBookingLoanController controller;

    @Mock
    private MobileAppBookingLoanService mobileAppBookingLoanService;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetBookingLoanIsOk() throws Exception {
        BaseResponseDto<BookingLoanResponseListsDto> baseResponseDto = new  BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        when(mobileAppBookingLoanService.getBookingLoan()).thenReturn(baseResponseDto);
        doRequestWithServiceMockedTest("/get/booking-loan",new BaseParamDto());
    }

    @Test
    public void shouldBookingLoanIsOk() throws Exception {
        BaseResponseDto<BookingLoanResponseListsDto> baseResponseDto = new  BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        when(mobileAppBookingLoanService.bookingLoan(any(BookingLoanRequestDto.class))).thenReturn(baseResponseDto);
        doRequestWithServiceMockedTest("/booking-loan",new BaseParamDto());
    }
}
