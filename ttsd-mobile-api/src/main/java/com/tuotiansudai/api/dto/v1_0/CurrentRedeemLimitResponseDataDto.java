package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class CurrentRedeemLimitResponseDataDto extends BaseResponseDataDto {


    @ApiModelProperty(value = "当日转出总限额", example = "100000")
    private long maxRedeemAmount;

    @ApiModelProperty(value = "当日剩余转出限额", example = "50000")
    private long avliableRedeemAmount;

    public long getMaxRedeemAmount() {
        return maxRedeemAmount;
    }

    public void setMaxRedeemAmount(long maxRedeemAmount) {
        this.maxRedeemAmount = maxRedeemAmount;
    }

    public long getAvliableRedeemAmount() {
        return avliableRedeemAmount;
    }

    public void setAvliableRedeemAmount(long avliableRedeemAmount) {
        this.avliableRedeemAmount = avliableRedeemAmount;
    }
}
