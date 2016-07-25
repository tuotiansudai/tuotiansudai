package com.tuotiansudai.service.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.BookingLoanPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BookingLoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BookingLoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BookingLoanService;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class BookingLoanServiceImpl implements BookingLoanService{
    @Autowired
    private BookingLoanMapper bookingLoanMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public BasePaginationDataDto<BookingLoanPaginationItemDataDto> bookingLoanList(ProductType productType, Date bookingTimeStartTime, Date bookingTimeEndTime, String mobile, Date noticeTimeStartTime, Date noticeTimeEndTime, Source source, Boolean status, Integer index, Integer pageSize) {
        if(bookingTimeStartTime != null){
            bookingTimeStartTime = new DateTime(bookingTimeStartTime).withTimeAtStartOfDay().toDate();
        }
        if (bookingTimeEndTime != null){
            bookingTimeEndTime = new DateTime(bookingTimeEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        if(noticeTimeStartTime != null){
            noticeTimeStartTime = new DateTime(noticeTimeStartTime).withTimeAtStartOfDay().toDate();
        }
        if (noticeTimeEndTime != null){
            noticeTimeEndTime = new DateTime(noticeTimeEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = bookingLoanMapper.findCountBookingLoanList(productType,bookingTimeStartTime,bookingTimeEndTime,mobile,noticeTimeStartTime,noticeTimeEndTime,source,status);
        int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
        index = index > totalPages ? totalPages : index;

        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(productType,bookingTimeStartTime,bookingTimeEndTime,mobile,noticeTimeStartTime,noticeTimeEndTime,source,status,(index - 1) * pageSize,pageSize);
        List<BookingLoanPaginationItemDataDto> items = Lists.newArrayList();
        if(count > 0){
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
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index,pageSize,count,items);
        basePaginationDataDto.setStatus(true);
        return basePaginationDataDto;
    }

    @Override
    public List<List<String>> getBookingLoanReportCsvData(ProductType productType, Date bookingTimeStartTime, Date bookingTimeEndTime, String mobile, Date noticeTimeStartTime, Date noticeTimeEndTime, Source source, Boolean status) {
        final int index = 1;
        final int pageSize = 9999999;
        long count = bookingLoanMapper.findCountBookingLoanList(productType,bookingTimeStartTime,bookingTimeEndTime,mobile,noticeTimeStartTime,noticeTimeEndTime,source,status);
        List<BookingLoanModel> bookingLoanModels = bookingLoanMapper.findBookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status, (index - 1) * pageSize, pageSize);
        List<BookingLoanPaginationItemDataDto> items = Lists.newArrayList();
        List<List<String>> csvData = new ArrayList<>();
        if(count > 0){
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
                List<String> dtoStrings = ExportCsvUtil.dtoToStringList(item);
                csvData.add(dtoStrings);
            }

        }
        return csvData;
    }
}
