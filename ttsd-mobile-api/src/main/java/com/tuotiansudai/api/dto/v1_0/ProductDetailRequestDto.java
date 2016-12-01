package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class ProductDetailRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "商品ID", example = "10")
    private String productId;

    @ApiModelProperty(value = "商品数量", example = "2")
    private Integer num;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
