package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanDto;
import com.tuotiansudai.api.dto.v1_0.BookingLoanListsDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import com.tuotiansudai.repository.model.ProductType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppBookingLoanServiceImpl implements MobileAppBookingLoanService{

    private static String[] booKingLoanList = new String[]{
            ProductType._90.toString() + "," + ProductType._90.getDuration() + ",11",
            ProductType._180.toString() + "," + ProductType._180.getDuration() + ",12",
            ProductType._360.toString() + "," + ProductType._360.getDuration() + ",13",
    };

    @Override
    public BaseResponseDto<BookingLoanListsDto> getBookingLoan() {
        BaseResponseDto<BookingLoanListsDto> baseResponseDto = new BaseResponseDto();
        BookingLoanListsDto bookingLoanListsDto = new BookingLoanListsDto();
        List<BookingLoanDto> bookingLoanDtoList = Lists.newArrayList();
        String[] splitBookLoan;
        for(String bookingLoan : booKingLoanList){
            splitBookLoan = bookingLoan.split(",");
            bookingLoanDtoList.add(new BookingLoanDto(splitBookLoan[0],splitBookLoan[1],splitBookLoan[2]));
        }
        bookingLoanListsDto.setBookingLoanDtoList(bookingLoanDtoList);
        baseResponseDto.setData(bookingLoanListsDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

}
