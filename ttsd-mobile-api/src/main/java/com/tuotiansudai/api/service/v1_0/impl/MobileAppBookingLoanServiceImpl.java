package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanListsDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import com.tuotiansudai.repository.model.ProductType;

public class MobileAppBookingLoanServiceImpl implements MobileAppBookingLoanService{

    private static String[] boolingloan = new String[]{
            ProductType._90.toString()
    };

    @Override
    public BaseResponseDto<BookingLoanListsDto> getBookingLoan() {
        BaseResponseDto<BookingLoanListsDto> baseResponseDto = new BaseResponseDto();
        BookingLoanListsDto bookingLoanListsDto = new BookingLoanListsDto();


        baseResponseDto.setData(bookingLoanListsDto);
        return baseResponseDto;
    }

}
