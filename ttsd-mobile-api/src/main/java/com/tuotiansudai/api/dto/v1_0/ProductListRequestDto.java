package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class ProductListRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "商品类型", example = "PHYSICAL,VIRTUAL")
    private String goodsType;

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
