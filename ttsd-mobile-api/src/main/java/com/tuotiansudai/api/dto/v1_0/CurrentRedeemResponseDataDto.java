package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class CurrentRedeemResponseDataDto extends BaseResponseDataDto {


    @ApiModelProperty(value = "提现金额", example = "100000")
    private long amount;


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
