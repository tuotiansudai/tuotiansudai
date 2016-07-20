package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.ProductType;

public class BookingLoanRequestDto extends BaseParamDto {

    private ProductType productType;
    private String bookingAmount;

    public BookingLoanRequestDto(){}

    public BookingLoanRequestDto(ProductType productType, String bookingAmount) {
        this.productType = productType;
        this.bookingAmount = bookingAmount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }
}
