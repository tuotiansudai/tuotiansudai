package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class CurrentRedeemDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
