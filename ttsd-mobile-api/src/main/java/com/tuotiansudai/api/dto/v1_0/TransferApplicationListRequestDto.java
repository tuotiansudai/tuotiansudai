package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class TransferApplicationListRequestDto extends TransferApplicationRequestDto {

    @ApiModelProperty(value = "最小利率", example = "10")
    private String rateLower;

    @ApiModelProperty(value = "最高利率", example = "10")
    private String rateUpper;

    public String getRateUpper() { return rateUpper; }

    public void setRateUpper(String rateUpper) { this.rateUpper = rateUpper; }

    public String getRateLower() { return rateLower; }

    public void setRateLower(String rateLower) { this.rateLower = rateLower; }

}

