package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class TransferCashDto implements Serializable {

    @NotEmpty
    private String loginName;

    @NotEmpty
    private String orderId;

    @NotEmpty
    private String amount;

    @NotEmpty
    private String cashSource;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCashSource() {
        return cashSource;
    }

    public void setCashSource(String cashSource) {
        this.cashSource = cashSource;
    }

    public TransferCashDto(String loginName, String orderId, String amount) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
    }

    public TransferCashDto(String loginName, String orderId, String amount, String cashSource) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
        this.cashSource = cashSource;
    }

    public TransferCashDto() {

    }

}
