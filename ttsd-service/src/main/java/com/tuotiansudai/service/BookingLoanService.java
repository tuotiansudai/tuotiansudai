package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.ProductType;

public interface BookingLoanService {
    void create(String phoneNum, ProductType productType,String bookingAmount);
}
