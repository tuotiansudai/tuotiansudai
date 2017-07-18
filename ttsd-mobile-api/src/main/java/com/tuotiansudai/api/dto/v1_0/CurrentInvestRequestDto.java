package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class CurrentInvestRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "投资金额", example = "5888800")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
