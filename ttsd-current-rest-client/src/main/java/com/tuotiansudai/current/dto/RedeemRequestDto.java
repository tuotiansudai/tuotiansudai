package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tuotiansudai.repository.model.Source;

public class RedeemRequestDto {

    private long id;

    @JsonProperty("login_name")
    private String loginName;

    private long amount;

    private Source source;

    public RedeemRequestDto(){

    }

    public RedeemRequestDto(String loginName, long amount, Source source) {
        this.loginName = loginName;
        this.amount = amount;
        this.source = source;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
