package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class CurrentRedeemLimitResponseDataDto extends BaseResponseDataDto {


    @ApiModelProperty(value = "当日转出总限额", example = "100000")
    private long totalAmount;

    @ApiModelProperty(value = "当日剩余转出限额", example = "50000")
    private long remainAmount;

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(long remainAmount) {
        this.remainAmount = remainAmount;
    }
}
