package com.tuotiansudai.api.dto.v2_0;


import java.io.Serializable;

public class ExtraRateListResponseDataDto implements Serializable{

    private String rate;

    private String amountLower;

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
