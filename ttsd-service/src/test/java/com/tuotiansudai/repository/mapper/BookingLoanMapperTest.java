package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:spring-security.xml"})
@Transactional
public class BookingLoanMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookingLoanMapper bookingLoanMapper;

    @Test
    public void shouldFindBookingLoanListByProductType(){
        UserModel userModel = createFakeUser();

        BookingLoanModel bookingLoanModel = fakeBookingLoan(userModel.getMobile(), new Date(), new Date());

        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(bookingLoanModel.getProductType(),null,null,null,null,null,null,false,0,10);
        assertEquals(1,bookingLoanModels.size());
        assertEquals(bookingLoanModel.getProductType(),bookingLoanModels.get(0).getProductType());
        assertEquals(bookingLoanModel.getMobile(),bookingLoanModels.get(0).getMobile());
        assertEquals(new DateTime(bookingLoanModel.getBookingTime()).toString("yyyy-MM-dd") ,new DateTime(bookingLoanModels.get(0).getBookingTime()).toString("yyyy-MM-dd"));
        assertEquals(new DateTime(bookingLoanModel.getNoticeTime()).toString("yyyy-MM-dd") ,new DateTime(bookingLoanModels.get(0).getNoticeTime()).toString("yyyy-MM-dd"));

    }
    @Test
    public void shouldFindBookingLoanListByBookingTime(){
        UserModel userModel = createFakeUser();
        Date bookingTime = new DateTime(2016,9,30,0,0,0).toDate();
        BookingLoanModel bookingLoanModel = fakeBookingLoan(userModel.getMobile(), bookingTime, new Date());

        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(bookingLoanModel.getProductType(),bookingTime,bookingTime,null,null,null,null,false,0,10);
        assertEquals(1,bookingLoanModels.size());
        assertEquals(bookingLoanModel.getProductType(),bookingLoanModels.get(0).getProductType());
        assertEquals(bookingLoanModel.getMobile(),bookingLoanModels.get(0).getMobile());
        assertEquals(new DateTime(bookingLoanModel.getBookingTime()).toString("yyyy-MM-dd") ,new DateTime(bookingLoanModels.get(0).getBookingTime()).toString("yyyy-MM-dd"));
        assertEquals(new DateTime(bookingLoanModel.getNoticeTime()).toString("yyyy-MM-dd") ,new DateTime(bookingLoanModels.get(0).getNoticeTime()).toString("yyyy-MM-dd"));
    }

    @Test
    public void shouldFindBookingLoanListByNoticeTime(){
        UserModel userModel = createFakeUser();
        Date noticeTime = new DateTime(2016,9,30,0,0,0).toDate();
        BookingLoanModel bookingLoanModel = fakeBookingLoan(userModel.getMobile(), new Date(), noticeTime);

        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(bookingLoanModel.getProductType(),null,noticeTime,null,null,null,null,false,0,10);
        assertEquals(1,bookingLoanModels.size());
        assertEquals(bookingLoanModel.getProductType(),bookingLoanModels.get(0).getProductType());
        assertEquals(bookingLoanModel.getMobile(),bookingLoanModels.get(0).getMobile());
        assertEquals(new DateTime(bookingLoanModel.getBookingTime()).toString("yyyy-MM-dd") ,new DateTime(bookingLoanModels.get(0).getBookingTime()).toString("yyyy-MM-dd"));
        assertEquals(new DateTime(bookingLoanModel.getNoticeTime()).toString("yyyy-MM-dd") ,new DateTime(bookingLoanModels.get(0).getNoticeTime()).toString("yyyy-MM-dd"));
    }

    private UserModel createFakeUser() {
        UserModel model = new UserModel();
        model.setLoginName("loginName");
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("13900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }

    private BookingLoanModel fakeBookingLoan(String mobile,Date bookingTime,Date noticeTime){
        BookingLoanModel bookingLoanModel = new BookingLoanModel();
        bookingLoanModel.setMobile(mobile);
        bookingLoanModel.setSource(Source.IOS);
        bookingLoanModel.setBookingTime(bookingTime);
        bookingLoanModel.setProductType(ProductType._180);
        bookingLoanModel.setAmount(500l);
        bookingLoanModel.setNoticeTime(noticeTime);
        bookingLoanModel.setStatus(false);
        bookingLoanModel.setCreateTime(new Date());
        bookingLoanModel.setUpdateTime(new Date());
        bookingLoanMapper.create(bookingLoanModel);
        return bookingLoanModel;
    }
}
