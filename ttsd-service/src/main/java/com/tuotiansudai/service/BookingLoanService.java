package com.tuotiansudai.service;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.BookingLoanPaginationItemDataDto;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public interface BookingLoanService {

    BasePaginationDataDto<BookingLoanPaginationItemDataDto> bookingLoanList(ProductType productType,Date bookingTimeStartTime,
                                           Date bookingTimeEndTime,String mobile,
                                           Date noticeTimeStartTime,Date noticeTimeEndTime,
                                           Source source,
                                           Boolean status,Integer index,Integer pageSize);

    List<List<String>> getBookingLoanReportCsvData(ProductType productType,Date bookingTimeStartTime,
                                            Date bookingTimeEndTime,String mobile,
                                            Date noticeTimeStartTime,Date noticeTimeEndTime,
                                            Source source,
                                            Boolean status);
}
