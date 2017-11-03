package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBookingLoanService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MobileAppBookingLoanServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppBookingLoanService mobileAppBookingLoanService;
    @Autowired
    private FakeUserHelper userMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldGetBookingLoanIsOk() {
        BaseResponseDto<BookingLoanResponseListsDto> bookingLoan = mobileAppBookingLoanService.getBookingLoan();
        assertThat(bookingLoan.getData().getBookingLoans().size(), is(3));
    }

    @Test
    public void shouldBookingLoanIsOk() {
        String loginName = "testBookingLoanUser";
        BookingLoanRequestDto bookingLoanRequestDto = new BookingLoanRequestDto();
        bookingLoanRequestDto.setBookingAmount("100");
        bookingLoanRequestDto.setProductType(ProductType._90);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPlatform("WEB");
        baseParam.setPhoneNum("13900000000");
        bookingLoanRequestDto.setBaseParam(baseParam);
        getUserModelTest(loginName);
        getAccountModel(loginName);
        BaseResponseDto baseResponseDto = mobileAppBookingLoanService.bookingLoan(bookingLoanRequestDto);
        assertEquals(baseResponseDto.getCode(), ReturnMessage.SUCCESS.getCode());
    }


    public UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    public AccountModel getAccountModel(String loginName) {
        AccountModel model = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(model);
        return model;
    }
}
