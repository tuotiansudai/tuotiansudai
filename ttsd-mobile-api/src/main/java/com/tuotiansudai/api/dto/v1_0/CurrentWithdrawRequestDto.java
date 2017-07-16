package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class CurrentWithdrawRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "提现金额", example = "10000")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
