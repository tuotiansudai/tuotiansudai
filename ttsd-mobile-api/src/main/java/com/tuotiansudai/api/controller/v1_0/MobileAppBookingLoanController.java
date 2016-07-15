package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanListsDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppBookingLoanController extends MobileAppBaseController {

    @Autowired
    private MobileAppBookingLoanService mobileAppBookingLoanService;

    @RequestMapping(value = "/get/booking-loan", method = RequestMethod.POST)
    public BaseResponseDto<BookingLoanListsDto> getBookingLoan(){
        return mobileAppBookingLoanService.getBookingLoan();
    }
}
