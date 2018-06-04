package com.tuotiansudai.repository.model;


import java.io.Serializable;

public enum BankRechargeStatus implements Serializable{

    WAIT_PAY("等待支付"),
    SUCCESS("充值成功"),
    FAIL("充值失败");

    private final String description;

    BankRechargeStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
