package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.BookingLoanPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BookingLoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.BookingLoanService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class BookingLoanServiceImpl implements BookingLoanService {

    @Autowired
    private BookingLoanMapper bookingLoanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    public void create(String loginName, ProductType productType, String bookingAmount) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        BookingLoanModel bookingLoanModel = new BookingLoanModel(
                userModel != null ? userModel.getMobile() : null,
                Source.WEB,
                DateTime.now().toDate(),
                productType,
                AmountConverter.convertStringToCent(bookingAmount),
                DateTime.now().toDate(),
                false,
                DateTime.now().toDate());
        bookingLoanMapper.create(bookingLoanModel);
    }

    @Override
    public BasePaginationDataDto<BookingLoanPaginationItemDataDto> bookingLoanList(ProductType productType, Date bookingTimeStartTime, Date bookingTimeEndTime, String mobile, Date noticeTimeStartTime, Date noticeTimeEndTime, Source source, Boolean status, Integer index, Integer pageSize) {
        if (bookingTimeStartTime != null) {
            bookingTimeStartTime = new DateTime(bookingTimeStartTime).withTimeAtStartOfDay().toDate();
        }
        if (bookingTimeEndTime != null) {
            bookingTimeEndTime = new DateTime(bookingTimeEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        if (noticeTimeStartTime != null) {
            noticeTimeStartTime = new DateTime(noticeTimeStartTime).withTimeAtStartOfDay().toDate();
        }
        if (noticeTimeEndTime != null) {
            noticeTimeEndTime = new DateTime(noticeTimeEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = bookingLoanMapper.findCountBookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status);
        int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
        index = index > totalPages ? totalPages : index;

        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status, (index - 1) * pageSize, pageSize);
        List<BookingLoanPaginationItemDataDto> items = Lists.newArrayList();
        if (count > 0) {
            items = Lists.transform(bookingLoanModels, new com.google.common.base.Function<BookingLoanModel, BookingLoanPaginationItemDataDto>() {
                @Override
                public BookingLoanPaginationItemDataDto apply(BookingLoanModel bookingLoanModel) {
                    BookingLoanPaginationItemDataDto bookingLoanPaginationItemDataDto = new BookingLoanPaginationItemDataDto(bookingLoanModel);
                    String loginName = userMapper.findByMobile(bookingLoanPaginationItemDataDto.getMobile()).getLoginName();
                    AccountModel accountModel = accountMapper.findByLoginName(loginName);
                    bookingLoanPaginationItemDataDto.setUserName(accountModel.getUserName());
                    return bookingLoanPaginationItemDataDto;
                }
            });

        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, items);
        basePaginationDataDto.setStatus(true);
        return basePaginationDataDto;
    }

    @Override
    public List<List<String>> getBookingLoanReportCsvData(ProductType productType, Date bookingTimeStartTime, Date bookingTimeEndTime, String mobile, Date noticeTimeStartTime, Date noticeTimeEndTime, Source source, Boolean status) {
        final int index = 1;
        final int pageSize = 9999999;
        long count = bookingLoanMapper.findCountBookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status);
        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status, (index - 1) * pageSize, pageSize);
        List<BookingLoanPaginationItemDataDto> items = Lists.newArrayList();
        List<List<String>> csvData = new ArrayList<>();
        if (count > 0) {
            items = Lists.transform(bookingLoanModels, new com.google.common.base.Function<BookingLoanModel, BookingLoanPaginationItemDataDto>() {
                @Override
                public BookingLoanPaginationItemDataDto apply(BookingLoanModel bookingLoanModel) {
                    BookingLoanPaginationItemDataDto bookingLoanPaginationItemDataDto = new BookingLoanPaginationItemDataDto(bookingLoanModel);
                    String loginName = userMapper.findByMobile(bookingLoanPaginationItemDataDto.getMobile()).getLoginName();
                    AccountModel accountModel = accountMapper.findByLoginName(loginName);
                    bookingLoanPaginationItemDataDto.setUserName(accountModel.getUserName());
                    return bookingLoanPaginationItemDataDto;
                }
            });
            for (BookingLoanPaginationItemDataDto item : items) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(item.getUserName());
                dataModel.add(item.getMobile());
                dataModel.add(item.getSource().name());
                dataModel.add(new DateTime(item.getBookingTime()).toString("yyyy-MM-dd HH:mm:ss"));
                dataModel.add(item.getProductType().name());
                dataModel.add(item.getAmount());
                dataModel.add(item.getNoticeTime() == null ? "" : new DateTime(item.getNoticeTime()).toString("yyyy-MM-dd HH:mm:ss"));
                dataModel.add(item.isStatus() ? "已通知" : "队列中");
                csvData.add(dataModel);
            }

        }
        return csvData;
    }

    @Override
    public void noticeBookingLoan(long bookingLoanId) {
        BookingLoanModel bookingLoanModel = bookingLoanMapper.findById(bookingLoanId);
        bookingLoanModel.setStatus(true);
        bookingLoanModel.setNoticeTime(new Date());
        bookingLoanModel.setUpdateTime(new Date());
        bookingLoanMapper.update(bookingLoanModel);
    }

    @Override
    public List<BookingLoanSumAmountView> findBookingLoanSumAmountByProductType(ProductType productType, Date bookingTimeStartTime,
                                                                   Date bookingTimeEndTime, String mobile,
                                                                   Date noticeTimeStartTime, Date noticeTimeEndTime,
                                                                   Source source){
        if (bookingTimeStartTime != null) {
            bookingTimeStartTime = new DateTime(bookingTimeStartTime).withTimeAtStartOfDay().toDate();
        }
        if (bookingTimeEndTime != null) {
            bookingTimeEndTime = new DateTime(bookingTimeEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        if (noticeTimeStartTime != null) {
            noticeTimeStartTime = new DateTime(noticeTimeStartTime).withTimeAtStartOfDay().toDate();
        }
        if (noticeTimeEndTime != null) {
            noticeTimeEndTime = new DateTime(noticeTimeEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<BookingLoanSumAmountView>  bookingLoanSumAmountViewList = bookingLoanMapper.findBookingLoanSumAmountByProductType(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source);

        Iterator<BookingLoanSumAmountView> transform = Iterators.transform(bookingLoanSumAmountViewList.iterator(), new Function<BookingLoanSumAmountView, BookingLoanSumAmountView>() {
            @Override
            public BookingLoanSumAmountView apply(BookingLoanSumAmountView input) {
                input.setSumAmount(AmountConverter.convertCentToString(Long.parseLong(input.getSumAmount())));
                return input;
            }
        });
        return Lists.newArrayList(transform);
    }
}
