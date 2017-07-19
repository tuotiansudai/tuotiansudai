package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;

public class DepositRequestDto {
    private long id;
    @JsonProperty("login_name")
    private String loginName;
    private long amount;
    private Source source;
    @JsonProperty("no_password")
    private boolean noPassword;

    public DepositRequestDto() {
    }

    public DepositRequestDto(String loginName, long amount, Source source, boolean noPassword) {
        this.loginName = loginName;
        this.amount = amount;
        this.source = source;
        this.noPassword = noPassword;
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

    public boolean getNoPassword() {
        return noPassword;
    }
}
