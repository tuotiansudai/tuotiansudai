package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.BookingLoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingLoanMapper {
    void create(BookingLoanModel bookingLoanModel);

    void update(BookingLoanModel bookingLoanModel);

    List<BookingLoanModel> selectBookingLoanByMobile(@Param(value = "mobile") String mobile);

    BookingLoanModel findById(long bookingLoanId);

    List<BookingLoanModel> findBookingLoanList(@Param(value = "productType") ProductType productType,
                                               @Param(value = "bookingTimeStartTime") Date bookingTimeStartTime,
                                               @Param(value = "bookingTimeEndTime") Date bookingTimeEndTime,
                                               @Param(value = "mobile") String mobile,
                                               @Param(value = "noticeTimeStartTime") Date noticeTimeStartTime,
                                               @Param(value="noticeTimeEndTime") Date noticeTimeEndTime,
                                               @Param(value = "source") Source source,
                                               @Param(value = "status" ) Boolean status,
                                               @Param(value = "index") Integer index,
                                               @Param(value = "pageSize") Integer pageSize);

    long findCountBookingLoanList(@Param(value = "productType") ProductType productType,
                                  @Param(value = "bookingTimeStartTime") Date bookingTimeStartTime,
                                  @Param(value = "bookingTimeEndTime") Date bookingTimeEndTime,
                                  @Param(value = "mobile") String mobile,
                                  @Param(value = "noticeTimeStartTime") Date noticeTimeStartTime,
                                  @Param(value="noticeTimeEndTime") Date noticeTimeEndTime,
                                  @Param(value = "source") Source source,
                                  @Param(value = "status" ) Boolean status);

}
