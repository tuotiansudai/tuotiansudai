package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class BindBankCardDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String cardNumber;

    private String loginName;

    private Source source = Source.WEB;

    private boolean fastPay = false;

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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
    }
}
