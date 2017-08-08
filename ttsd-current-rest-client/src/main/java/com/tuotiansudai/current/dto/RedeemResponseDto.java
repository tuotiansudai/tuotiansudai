package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RedeemResponseDto {

    private long id;

    @JsonProperty(value = "login_name")
    private String loginName;

    @JsonProperty(value = "amount")
    private long amount;

    @JsonProperty(value = "source")
    private String source;

    @JsonProperty(value = "status")
    private String status;

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }
}
