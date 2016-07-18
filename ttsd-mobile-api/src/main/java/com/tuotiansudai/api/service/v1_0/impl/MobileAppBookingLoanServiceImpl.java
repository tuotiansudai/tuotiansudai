package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BookingLoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class MobileAppBookingLoanServiceImpl implements MobileAppBookingLoanService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BookingLoanMapper bookingLoanMapper;

    private static String[] booKingLoanList = new String[]{
            ProductType._90.toString() + "," + ProductType._90.getDuration() + ",11",
            ProductType._180.toString() + "," + ProductType._180.getDuration() + ",12",
            ProductType._360.toString() + "," + ProductType._360.getDuration() + ",13",
    };

    @Override
    public BaseResponseDto<BookingLoanResponseListsDto> getBookingLoan() {
        BaseResponseDto<BookingLoanResponseListsDto> baseResponseDto = new BaseResponseDto();
        BookingLoanResponseListsDto bookingLoanListsDto = new BookingLoanResponseListsDto();
        List<BookingLoanResponseDto> bookingLoanResponseDtoList = Lists.newArrayList();
        String[] splitBookLoan;
        for(String bookingLoan : booKingLoanList){
            splitBookLoan = bookingLoan.split(",");
            bookingLoanResponseDtoList.add(new BookingLoanResponseDto(splitBookLoan[0],splitBookLoan[1],splitBookLoan[2]));
        }
        bookingLoanListsDto.setBookingLoanResponseDtoList(bookingLoanResponseDtoList);
        baseResponseDto.setData(bookingLoanListsDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto bookingLoan(BookingLoanRequestDto bookingLoanRequestDto){
        UserModel userModel = userMapper.findByLoginName(bookingLoanRequestDto.getBaseParam().getUserId());
        BookingLoanModel bookingLoanModel = new BookingLoanModel(userModel,
                accountMapper.findByLoginName(bookingLoanRequestDto.getBaseParam().getUserId()).getUserName(),
                bookingLoanRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH),
                DateTime.now().toDate(),
                bookingLoanRequestDto.getProductType(),
                !Strings.isNullOrEmpty(bookingLoanRequestDto.getBookingAmount()) ? Long.parseLong(bookingLoanRequestDto.getBookingAmount()) : 0,
                DateTime.now().toDate(),
                false);
        bookingLoanMapper.create(bookingLoanModel);
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

}
