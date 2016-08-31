package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;

public class ProductOrderResponseDto{

    private String productId;

    private String productName;

    private String productNum;

    private String orderTime;

    public ProductOrderResponseDto(String productId, String productName, String productNum, String orderTime) {
        this.productId = productId;
        this.productName = productName;
        this.productNum = productNum;
        this.orderTime = orderTime;
    }

    public ProductOrderResponseDto(ProductOrderViewDto productOrderViewDto){
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
