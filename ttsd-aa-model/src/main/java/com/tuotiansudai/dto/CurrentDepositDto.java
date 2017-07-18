package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CurrentDepositDto implements Serializable {

    @JsonProperty(value = "id")
    private long id;

    @NotEmpty
    @JsonProperty(value = "login_name")
    private String loginName;

    @JsonProperty(value = "amount")
    private long amount;

    @NotNull
    @JsonProperty(value = "source")
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
