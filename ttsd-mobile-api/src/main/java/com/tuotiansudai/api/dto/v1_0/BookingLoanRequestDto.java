package com.tuotiansudai.api.dto.v1_0;


public class BookingLoanRequestDto extends BaseParamDto {

    private String productType;
    private String bookingAmount;

    public BookingLoanRequestDto(){}

    public BookingLoanRequestDto(String productType, String bookingAmount) {
        this.productType = productType;
        this.bookingAmount = bookingAmount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }
}
