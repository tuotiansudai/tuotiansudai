package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class CurrentRedeemDto implements Serializable {

    @NotEmpty
    @JsonProperty(value = "login_name")
    private String loginName;

    @NotEmpty
    @JsonProperty(value = "amount")
    private long amount;

    @NotNull
    @JsonProperty(value = "source")
    private Source source;

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
