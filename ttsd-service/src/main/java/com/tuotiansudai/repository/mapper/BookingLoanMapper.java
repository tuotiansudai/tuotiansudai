package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.BookingLoanModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingLoanMapper {
    void create(BookingLoanModel bookingLoanModel);
}
