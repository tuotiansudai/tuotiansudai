package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class ProductOrderResponseDto {

    @ApiModelProperty(value = "商品id", example = "1")
    private String productId;

    @ApiModelProperty(value = "商品名称", example = "会员")
    private String productName;

    @ApiModelProperty(value = "商品数量", example = "199")
    private String productNum;

    @ApiModelProperty(value = "兑换时间", example = "2016-11-15 14:03:01")
    private String orderTime;

    public ProductOrderResponseDto(ProductOrderViewDto productOrderViewDto) {
        this.productId = String.valueOf(productOrderViewDto.getId());
        this.productName = productOrderViewDto.getName();
        this.productNum = String.valueOf(productOrderViewDto.getNum());
        this.orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(productOrderViewDto.getCreatedTime());
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
