package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;

public class RedeemDetailResponseDto implements Serializable {

    private long id;

    @JsonProperty(value = "login_name")
    private String loginName;

    private long amount;

    private Source source;

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
