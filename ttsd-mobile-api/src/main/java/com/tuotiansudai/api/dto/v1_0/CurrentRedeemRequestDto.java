package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class CurrentRedeemRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "转出金额", example = "10000")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
