package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.BookingLoanModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingLoanMapper {
    void create(BookingLoanModel bookingLoanModel);

    List<BookingLoanModel> selectBookingLoanByMobile(@Param(value = "mobile") String mobile);
}
