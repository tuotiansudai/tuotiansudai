package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RedeemDataDto extends BaseDataDto {

    @JsonProperty(value = "amount")
    private long amount;

    public long getAmount() {
        return amount;
    }

}
