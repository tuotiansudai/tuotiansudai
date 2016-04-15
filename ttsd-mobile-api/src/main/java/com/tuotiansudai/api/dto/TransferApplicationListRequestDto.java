package com.tuotiansudai.api.dto;

public class TransferApplicationListRequestDto extends TransferApplicationRequestDto {
    private String rateLower;
    private String rateUpper;

    public String getRateUpper() { return rateUpper; }

    public void setRateUpper(String rateUpper) { this.rateUpper = rateUpper; }

    public String getRateLower() { return rateLower; }

    public void setRateLower(String rateLower) { this.rateLower = rateLower; }
}

