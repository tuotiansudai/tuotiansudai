package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class BookingLoanResponseDto {

    @ApiModelProperty(value = "可预约标的类型", example = "_30, _90, _180, _360")
    private String productType;

    @ApiModelProperty(value = "项目期限", example = "30, 90, 180, 360")
    private String duration;

    @ApiModelProperty(value = "预计年化收益率", example = "11")
    private String rate;

    public BookingLoanResponseDto(String productType, String duration, String rate) {
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
