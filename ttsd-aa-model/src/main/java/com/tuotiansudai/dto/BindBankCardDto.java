package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class BindBankCardDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String cardNumber;

    private String bankCode;

    private String loginName;

    private Source source = Source.WEB;

    private String deviceId;

    private String ip;

    private boolean fastPay = false;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
    }
}
