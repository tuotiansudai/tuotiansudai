package com.tuotiansudai.api.dto.v1_0;


public class BookingLoanDto {

    private String productType;
    private String duration;
    private String rate;

    public BookingLoanDto(String productType, String duration, String rate) {
        this.productType = productType;
        this.duration = duration;
        this.rate = rate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
