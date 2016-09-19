package com.tuotiansudai.api.dto.v1_0;

public class ProductDetailRequestDto extends BaseParamDto {

    private String productId;

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
