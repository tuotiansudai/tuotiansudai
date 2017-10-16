package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class BookingLoanMapperTest {

    @Autowired
    private BookingLoanMapper bookingLoanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Test
    public void shouldCreateIsOk() {
        UserModel userModel = getUserModelTest();
        BookingLoanModel bookingLoanModel = new BookingLoanModel(userModel.getLoginName(), userModel.getMobile(),
                Source.WEB,
                DateTime.now().toDate(),
                ProductType._90,
                1000L,
                DateTime.now().toDate(),
                false,
                DateTime.now().toDate());
        bookingLoanMapper.create(bookingLoanModel);

        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.selectBookingLoanByMobile(userModel.getMobile());
        assertTrue(CollectionUtils.isNotEmpty(bookingLoanModels));
        assertTrue(bookingLoanModels.size() == 1);
        assertTrue(bookingLoanModels.get(0).getMobile().equals(bookingLoanModel.getMobile()));
    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(RandomStringUtils.randomNumeric(11));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
