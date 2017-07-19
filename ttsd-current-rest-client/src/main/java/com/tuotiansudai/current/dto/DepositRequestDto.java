package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;

public class DepositRequestDto {
    private long id;
    @JsonProperty("login_name")
    private String loginName;
    private long amount;
    private Source source;

    public DepositRequestDto() {
    }

    public DepositRequestDto(String loginName, long amount, Source source) {
        this.loginName = loginName;
        this.amount = amount;
        this.source = source;
    }

    public long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getAmount() {
        return amount;
    }

    public Source getSource() {
        return source;
    }
}
