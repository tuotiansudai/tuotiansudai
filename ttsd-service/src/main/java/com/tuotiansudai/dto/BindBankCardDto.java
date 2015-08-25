package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class BindBankCardDto implements Serializable {
    @NotEmpty
    private String cardNumber;

    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
