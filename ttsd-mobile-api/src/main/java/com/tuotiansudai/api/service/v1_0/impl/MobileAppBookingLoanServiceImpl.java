package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import com.tuotiansudai.repository.mapper.BookingLoanMapper;
import com.tuotiansudai.repository.model.BookingLoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class MobileAppBookingLoanServiceImpl implements MobileAppBookingLoanService {

    @Autowired
    private BookingLoanMapper bookingLoanMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseResponseDto<BookingLoanResponseListsDto> getBookingLoan() {
        BaseResponseDto<BookingLoanResponseListsDto> baseResponseDto = new BaseResponseDto();
        BookingLoanResponseListsDto bookingLoanListsDto = new BookingLoanResponseListsDto();
        List<BookingLoanResponseDto> bookingLoanResponseDtoList = Lists.newArrayList();
        bookingLoanResponseDtoList.add(new BookingLoanResponseDto(ProductType._90.toString(), String.valueOf(ProductType._90.getDuration()), "11"));
        bookingLoanResponseDtoList.add(new BookingLoanResponseDto(ProductType._180.toString(), String.valueOf(ProductType._180.getDuration()), "12"));
        bookingLoanResponseDtoList.add(new BookingLoanResponseDto(ProductType._360.toString(), String.valueOf(ProductType._360.getDuration()), "13"));
        bookingLoanListsDto.setBookingLoans(bookingLoanResponseDtoList);
        baseResponseDto.setData(bookingLoanListsDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto bookingLoan(BookingLoanRequestDto bookingLoanRequestDto) {
        UserModel userModel = userMapper.findByMobile(bookingLoanRequestDto.getBaseParam().getPhoneNum());

        BookingLoanModel bookingLoanModel = new BookingLoanModel(
                userModel != null ? userModel.getLoginName() : null,
                bookingLoanRequestDto.getBaseParam().getPhoneNum(),
                Source.valueOf(bookingLoanRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH)),
                DateTime.now().toDate(),
                bookingLoanRequestDto.getProductType(),
                AmountConverter.convertStringToCent(bookingLoanRequestDto.getBookingAmount()),
                DateTime.now().toDate(),
                false,
                DateTime.now().toDate());
        bookingLoanMapper.create(bookingLoanModel);
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}
