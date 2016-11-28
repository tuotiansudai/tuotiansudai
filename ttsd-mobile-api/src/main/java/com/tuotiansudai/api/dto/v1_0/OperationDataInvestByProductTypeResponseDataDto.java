package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class OperationDataInvestByProductTypeResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的期限", example = "30天")
    private String name;
    @ApiModelProperty(value = "标的金额,单位为分", example = "2999292")
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
