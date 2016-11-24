package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.ProductType;
import io.swagger.annotations.ApiModelProperty;

public class BookingLoanRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "标的类型", example = "_30")
    private ProductType productType;

    @ApiModelProperty(value = "预约金额", example = "100")
    private String bookingAmount;

    public BookingLoanRequestDto() {
    }

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
