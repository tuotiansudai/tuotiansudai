package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.BookingLoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.BookingLoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BookingLoanService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingLoanServiceImpl implements BookingLoanService{

    @Autowired
    private BookingLoanMapper bookingLoanMapper;

    @Autowired
    private UserMapper userMapper;

    public void create(String loginName, ProductType productType, String bookingAmount){
        BookingLoanModel bookingLoanModel = new BookingLoanModel(userMapper.findUsersMobileByLoginName(loginName),
                Source.WEB,
                DateTime.now().toDate(),
                productType,
                AmountConverter.convertStringToCent(bookingAmount),
                DateTime.now().toDate(),
                false,
                DateTime.now().toDate());
        bookingLoanMapper.create(bookingLoanModel);
    }
}
