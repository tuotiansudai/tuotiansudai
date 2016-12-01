package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanResponseListsDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "预约标的")
public class MobileAppBookingLoanController extends MobileAppBaseController {

    @Autowired
    private MobileAppBookingLoanService mobileAppBookingLoanService;

    @RequestMapping(value = "/get/booking-loan", method = RequestMethod.POST)
    @ApiOperation("获取可预约标的")
    public BaseResponseDto<BookingLoanResponseListsDto> getBookingLoan() {
        return mobileAppBookingLoanService.getBookingLoan();
    }

    @RequestMapping(value = "/booking-loan", method = RequestMethod.POST)
    @ApiOperation("预约标的")
    public BaseResponseDto bookingLoan(@RequestBody BookingLoanRequestDto bookingLoanRequestDto) {
        return mobileAppBookingLoanService.bookingLoan(bookingLoanRequestDto);
    }


}
