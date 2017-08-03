package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;

public class InterestSettlementRequestDto {
    @JsonProperty("login_name")
    private String loginName;
    private long amount;

    public InterestSettlementRequestDto() {
    }

    public InterestSettlementRequestDto(String loginName, long amount) {
        this.loginName = loginName;
        this.amount = amount;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getAmount() {
        return amount;
    }

}
