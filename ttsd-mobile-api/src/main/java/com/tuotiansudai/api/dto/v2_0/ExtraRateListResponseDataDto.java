package com.tuotiansudai.api.dto.v2_0;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ExtraRateListResponseDataDto implements Serializable {

    @ApiModelProperty(value = "利率", example = "10")
    private String rate;

    @ApiModelProperty(value = "最少金额", example = "100")
    private String amountLower;

    @ApiModelProperty(value = "最大金额", example = "200")
    private String amountUpper;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmountLower() {
        return amountLower;
    }

    public void setAmountLower(String amountLower) {
        this.amountLower = amountLower;
    }

    public String getAmountUpper() {
        return amountUpper;
    }

    public void setAmountUpper(String amountUpper) {
        this.amountUpper = amountUpper;
    }
}
