package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class TransferCashDto implements Serializable{

    @NotEmpty
    private String loginName;

    @NotEmpty
    private String orderId;

    @NotEmpty
    private String amount;

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

    public TransferCashDto(String loginName, String orderId, String amount) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
    }

    public TransferCashDto() {

    }

}
